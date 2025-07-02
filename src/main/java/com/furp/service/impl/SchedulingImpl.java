package com.furp.service.impl;

import com.furp.DTO.response.PendingReviewDto;
import com.furp.entity.AnnualReview;
import com.furp.entity.PhdSkill;
import com.furp.entity.Room;
import com.furp.entity.Teacher;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.RoomMapper;
import com.furp.mapper.TeacherMapper;
import com.furp.mapper.TeacherSkillMapper;
import com.furp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Service
public class SchedulingImpl implements SchedulingService {

    @Autowired private PhdMapper phdMapper;
    @Autowired private TeacherMapper teacherMapper;
    @Autowired private RoomMapper roomMapper;
    @Autowired private AnnualReviewService annualReviewService;
    @Autowired private TeacherService teacherService;
    @Autowired private PhdSkillService phdSkillService;
    @Autowired private TeacherSkillService teacherSkillService;
    @Autowired
    private TeacherSkillMapper teacherSkillMapper;

    // TimeSlot class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeSlot {
        private LocalDateTime startTime; // 开始时间
        private LocalDateTime endTime;   // 结束时间

    }

    // PotentialAssignment class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PotentialAssignment {
        private int phdId;
        private int teacher1Id;
        private int teacher2Id;
        private TimeSlot timeSlot;
        private int skillScore;
    }

    @Override
    public void autoSchedule() {
        List<PendingReviewDto> pendingReviews = annualReviewService.getPendingReviews();
        List<Integer> pendingPhdIds = pendingReviews.stream()
                .map(PendingReviewDto::getPhdId)
                .collect(Collectors.toList());   //获取等待分配的phdId


        List<Teacher> teachers = teacherService.findAllTeacher();
        List<Room> allRooms = roomMapper.selectAllRooms();

        Map<Long, Integer> teacherWorkload = initWorkloadMap(teachers);
        Map<String, Set<TimeSlot>> busyMap = loadBusySlots();
        Set<Long> usedRooms = new HashSet<>();

        List<PotentialAssignment> pool = generatePotentialAssignments(pendingReviews, teachers, busyMap); // 方案池
        List<AnnualReview> finalResult = new ArrayList<>();

        while (!pendingPhdIds.isEmpty() && !pool.isEmpty()) {
            PotentialAssignment best = selectBest(pool, teacherWorkload);
            Room assignedRoom = allocateRoom(best.getTimeSlot(), usedRooms, allRooms, busyMap);
            if (assignedRoom == null) {
                pool.remove(best);
                continue;
            }
            finalResult.add(toAnnualReview(best, assignedRoom));
            updateBusy(busyMap, best, assignedRoom);
            updateWorkload(teacherWorkload, best);
            usedRooms.add(assignedRoom.getId());
            pendingPhdIds.removeIf(s -> s.getId().equals(best.getPhdId()));
            pool = removeConflicts(pool, best, assignedRoom);
        }

        // TODO: Replace with your mapper method
        finalResult.forEach(reviewMapper::insert);
    }

    private Map<Integer, Integer> initWorkloadMap(List<Teacher> teachers) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Teacher t : teachers) {
            map.put(t.getId(), 0);
        }
        return map;
    }

    private Map<String, Set<TimeSlot>> loadBusySlots() {
        return new HashMap<>(); // TODO: implement based on AnnualReview table
    }

    private List<PotentialAssignment> generatePotentialAssignments(List<PendingReviewDto> students, List<Teacher> teachers, Map<String, Set<TimeSlot>> busyMap) {

        List<PotentialAssignment> pool = new ArrayList<>(); // TODO: 实现组合生成逻辑
        for (PendingReviewDto review : students) {

            Integer phd = review.getPhdId();

            List<Teacher> eligibleAssessors = teacherService.findEligibleAssessors(phd);
            if (eligibleAssessors.size() < 2) {
                System.out.println("学生 ${review.getStudentName()} 的候选评审员不足两人，已跳过。");
                continue;
            }

            Set<Integer> phdSkills = phdSkillService.findPhdSkillsById(phd);

            for(int i = 0; i < eligibleAssessors.size()-1; i++){
                for(int j = i+1; j < eligibleAssessors.size(); j++){
                    Teacher t1 = eligibleAssessors.get(i);
                    Teacher t2 = eligibleAssessors.get(j);

                    Set<Integer> t1Skill = teacherSkillService.findTeacherSkillsById(t1.getId());
                    Set<Integer> t2Skill = teacherSkillService.findTeacherSkillsById(t2.getId());

                    long t1MatchCount = t1Skill.stream().filter(phdSkills::contains).count();
                    long t2MatchCount = t2Skill.stream().filter(phdSkills::contains).count();
                    int skillScore = (int) (t1MatchCount + t2MatchCount);
                }
            }




        }
    }

    private PotentialAssignment selectBest(List<PotentialAssignment> pool, Map<Long, Integer> workload) {

        return pool.get(0); // TODO: 加入打分排序逻辑
    }

    private Room allocateRoom(TimeSlot slot, Set<Long> used, List<Room> allRooms, Map<String, Set<TimeSlot>> busyMap) {
        for (Long id : used) {
            if (isFree("room-" + id, slot, busyMap)) {
                return allRooms.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
            }
        }
        for (Room r : allRooms) {
            if (!used.contains(r.getId()) && isFree("room-" + r.getId(), slot, busyMap)) {
                return r;
            }
        }
        return null;
    }

    private boolean isFree(String key, TimeSlot slot, Map<String, Set<TimeSlot>> busyMap) {
        return true; // TODO: 根据 TimeSlot 比较逻辑判断冲突
    }

    private AnnualReview toAnnualReview(PotentialAssignment p, Room room) {
        AnnualReview ar = new AnnualReview();
        ar.setPhdId(p.getPhdId());
        ar.setAssessor1Id(p.getTeacher1Id());
        ar.setAssessor2Id(p.getTeacher2Id());
        ar.setRoomId(room.getId());
        ar.setTimeSlot(p.getTimeSlot().toString()); // or better: encode start+end
        return ar;
    }

    private void updateBusy(Map<String, Set<TimeSlot>> map, PotentialAssignment p, Room room) {
        // TODO: 将 slot 写入 teacher1, teacher2 和 room 的 busyMap
    }

    private void updateWorkload(Map<Long, Integer> workload, PotentialAssignment p) {
        workload.put(p.getTeacher1Id(), workload.get(p.getTeacher1Id()) + 1);
        workload.put(p.getTeacher2Id(), workload.get(p.getTeacher2Id()) + 1);
    }

    private List<PotentialAssignment> removeConflicts(List<PotentialAssignment> pool, PotentialAssignment used, Room room) {
        return pool.stream().filter(p -> !Objects.equals(p.getPhdId(), used.getPhdId()))
                .collect(Collectors.toList());
    }
}
