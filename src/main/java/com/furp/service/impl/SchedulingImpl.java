package com.furp.service.impl;

import com.furp.DTO.response.PendingReviewDto;
import com.furp.entity.*;
import com.furp.mapper.*;
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
    @Autowired private AvailableTimeMapper availableTimeMapper;
    @Autowired private SchedulesMapper schedulesMapper;

    // TimeSlot class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeSlot {
        private LocalDateTime startTime; // 开始时间
        private LocalDateTime endTime;   // 结束时间


        public boolean overlaps(TimeSlot other){
            // 如果满足以下两个条件，则两个时间段有重叠：
            // 1. 当前时间段的开始时间在另一个时间段的结束时间之前
            // AND
            // 2. 另一个时间段的开始时间在当前时间段的结束时间之前
            return this.startTime.isBefore(other.endTime) &&
                    other.startTime.isBefore(this.endTime);
            //在这个 overlaps 方法中，this.startTime.isBefore(other.endTime) 确保了第一个时间段没有完全在第二个时间段之后。
            //而另一个条件 other.startTime.isBefore(this.endTime) 则确保了第二个时间段没有完全在第一个时间段之后。
            //这两个条件同时满足，才表示它们有重叠。
            //这个逻辑是处理时间或区间重叠问题的标准方法之一
        }

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
        //busyMap为资源名（String）对应 Timeslot集合 的键值对形式
        Map<String, Set<TimeSlot>> busyMap = new HashMap<>();

        //加载已经存在的未来日程
        List<Schedules> schedules = schedulesMapper.findAllFutureSchedules();

        for(Schedules schedule: schedules){
            //将该日程的时间段转化为Timeslot结构存储
            TimeSlot busySlot = new TimeSlot(schedule.getStartTime(), schedule.getEndTime());

            if(schedule.getTeacherId()!=null){
                String teacherKey = "Teacher-" + schedule.getTeacherId();
                busyMap.computeIfAbsent(teacherKey, k -> new HashSet<>()).add(busySlot);
                /**
                 * 对于给定的 teacherKey：
                 *
                 * 检查 busyMap 中是否已经有这个老师的忙碌时间表（一个 Set<TimeSlot>）。
                 *
                 * 如果没有，或者对应的值是 null，那么就为这个老师创建一个新的空的 HashSet<TimeSlot>，并将其作为值放入 busyMap 中。
                 *
                 * 无论 Set 是新创建的还是已经存在的，都将 busySlot 这个具体的时间段添加到该老师的忙碌时间 Set 中。
                 */

            }

            if(schedule.getRoomId()!=null){
                String roomKey = "room-" + schedule.getRoomId();
                busyMap.computeIfAbsent(roomKey, k -> new HashSet<>()).add(busySlot);
            }
        }


        return busyMap; // TODO: implement based on AnnualReview table
    }

    private List<PotentialAssignment> generatePotentialAssignments(List<PendingReviewDto> students, List<Teacher> teachers, Map<String, Set<TimeSlot>> busyMap) {

        List<PotentialAssignment> pool = new ArrayList<>(); // TODO: 实现组合生成逻辑
        for (PendingReviewDto review : students) {

            Integer phdId = review.getPhdId();

            List<Teacher> eligibleAssessors = teacherService.findEligibleAssessors(phdId);
            if (eligibleAssessors.size() < 2) {
                System.out.println("学生 ${review.getStudentName()} 的候选评审员不足两人，已跳过。");
                continue;
            }

            Set<Integer> phdSkills = phdSkillService.findPhdSkillsById(phdId);

            for(int i = 0; i < eligibleAssessors.size()-1; i++){
                for(int j = i+1; j < eligibleAssessors.size(); j++){
                    Teacher t1 = eligibleAssessors.get(i);
                    Teacher t2 = eligibleAssessors.get(j);

                    Set<Integer> t1Skill = teacherSkillService.findTeacherSkillsById(t1.getId());
                    Set<Integer> t2Skill = teacherSkillService.findTeacherSkillsById(t2.getId());

                    long t1MatchCount = t1Skill.stream().filter(phdSkills::contains).count();
                    long t2MatchCount = t2Skill.stream().filter(phdSkills::contains).count();
                    int skillScore = (int) (t1MatchCount + t2MatchCount);

                    List<AvailableTime> t1AvailableTime = availableTimeMapper.findByTeacherId(t1.getId());
                    List<AvailableTime> t2AvailableTime = availableTimeMapper.findByTeacherId(t2.getId());

                    List<TimeSlot> commonTimeSlots = findCommonTimeSlots(t1AvailableTime, t2AvailableTime);

                    for(TimeSlot slot : commonTimeSlots){

                        if(isFree("teacher-" + t1.getId(), slot, busyMap) &&
                            isFree("teacher-" + t2.getId(), slot, busyMap)){

                            PotentialAssignment pa = new PotentialAssignment(
                                    phdId,
                                    t1.getId(),
                                    t2.getId(),
                                    slot,
                                    skillScore
                            );

                            pool.add(pa);
                        }

                    }
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

    private boolean isFree(String key, TimeSlot targetSlot, Map<String, Set<TimeSlot>> busyMap) {
        Set<TimeSlot> busySlots= busyMap.get(key);
        if (busySlots.isEmpty()){
            return true;
        }
        return busySlots.stream().noneMatch(busySlot -> busySlot.overlaps(targetSlot));


        // TODO: 根据 TimeSlot 比较逻辑判断冲突
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

    private List<TimeSlot> findCommonTimeSlots(List<AvailableTime> list1, List<AvailableTime> list2) {
        List<TimeSlot> commonSlots = new ArrayList<>();
        Set<TimeSlot> slot1 = list1.stream()
                .map(at1 -> new TimeSlot(at1.getStart_time(), at1.getEnd_time()))
                .collect(Collectors.toSet());

        for(AvailableTime at2 : list2){
            TimeSlot slot2 = new TimeSlot(at2.getStart_time(), at2.getEnd_time());
            if(slot1.contains(slot2)){
                commonSlots.add(slot2);
            }
        }

        return commonSlots;

        }
}

