package com.furp.service.impl;

import com.furp.entity.AnnualReview;
import com.furp.entity.Phd;
import com.furp.entity.Room;
import com.furp.entity.Teacher;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.RoomMapper;
import com.furp.mapper.TeacherMapper;
import com.furp.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



import com.furp.entity.*;
import com.furp.mapper.*;
import com.furp.service.SchedulingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulingImpl implements SchedulingService {

    @Autowired private PhdMapper phdMapper;
    @Autowired private TeacherMapper teacherMapper;
    @Autowired private RoomMapper roomMapper;
    @Autowired private AnnualReviewMapper reviewMapper;

    // TimeSlot class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeSlot {
        private String day;
        private String start;
        private String end;
    }

    // PotentialAssignment class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PotentialAssignment {
        private Long phdId;
        private Long teacher1Id;
        private Long teacher2Id;
        private TimeSlot timeSlot;
        private int skillScore;
    }

    @Override
    public void autoSchedule() {
        List<Phd> pendingPhds = phdMapper.selectList(null); // stub
        List<Teacher> teachers = teacherMapper.selectList(null);
        List<Room> allRooms = roomMapper.selectList(null);

        Map<Long, Integer> teacherWorkload = initWorkloadMap(teachers);
        Map<String, Set<TimeSlot>> busyMap = loadBusySlots();
        Set<Long> usedRooms = new HashSet<>();

        List<PotentialAssignment> pool = generatePotentialAssignments(pendingPhds, teachers, busyMap);
        List<AnnualReview> finalResult = new ArrayList<>();

        while (!pendingPhds.isEmpty() && !pool.isEmpty()) {
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
            pendingPhds.removeIf(s -> s.getId().equals(best.getPhdId()));
            pool = removeConflicts(pool, best, assignedRoom);
        }

        // TODO: Replace with your mapper method
        finalResult.forEach(reviewMapper::insert);
    }

    private Map<Long, Integer> initWorkloadMap(List<Teacher> teachers) {
        Map<Long, Integer> map = new HashMap<>();
        for (Teacher t : teachers) map.put(t.getId(), 0);
        return map;
    }

    private Map<String, Set<TimeSlot>> loadBusySlots() {
        return new HashMap<>(); // TODO: implement based on AnnualReview table
    }

    private List<PotentialAssignment> generatePotentialAssignments(List<Phd> students, List<Teacher> teachers, Map<String, Set<TimeSlot>> busyMap) {
        return new ArrayList<>(); // TODO: 实现组合生成逻辑
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
