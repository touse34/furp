package com.furp.service.impl;


import com.furp.DTO.FinalAssignment;
import com.furp.DTO.PendingReviewDto;
import com.furp.DTO.PotentialAssignment;
import com.furp.DTO.TimeSlot;
import com.furp.entity.*;
import com.furp.mapper.*;
import com.furp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


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
    @Autowired private AnnualReviewMapper annualReviewMapper;


    public void autoSchedule() {
        List<PendingReviewDto> pendingReviews = annualReviewService.getPendingReviews();
        List<Integer> pendingPhdIds = pendingReviews.stream()
                .map(PendingReviewDto::getPhdId)
                .collect(Collectors.toList());   //获取等待分配的phdId


        List<Teacher> teachers = teacherService.findAllTeacher();
        List<Room> allRooms = roomMapper.selectAllRooms();

        // 检查是否有足够的房间
        if (allRooms.isEmpty()) {
            System.out.println("房间资源不足");
            return;  // 没有房间则提前退出
        }

        Map<Integer, Integer> teacherWorkload = initWorkloadMap(teachers);
        Map<String, Set<TimeSlot>> busyMap = loadBusySlots(); //busymap:一个资源名对应一个timeslot的set集合
        Set<Long> usedRooms = new HashSet<>();

        List<PotentialAssignment> pool = generatePotentialAssignments(pendingReviews, teachers, busyMap); // 方案池
        List<FinalAssignment> finalResult = new ArrayList<>();

        while (!pendingPhdIds.isEmpty() && !pool.isEmpty()) {
            PotentialAssignment best = selectBest(pool, teacherWorkload);
            Room assignedRoom = allocateRoom(best.getTimeSlot(), usedRooms, allRooms, busyMap);

            if (assignedRoom == null) {
                // 房间不足，跳过此任务并输出提示信息
                System.out.println("房间资源不足,跳过学生" + best.getPhdId());
                pool.remove(best);
                continue;
            }


            finalResult.add(toAnnualReview(best, assignedRoom));
            updateBusy(busyMap, best, assignedRoom);
            updateWorkload(teacherWorkload, best);
            usedRooms.add((long)assignedRoom.getId());

            pendingPhdIds.removeIf(s -> s.equals(best.getPhdId()));
            pool = removeConflicts(pool, best, assignedRoom);
        }

        // TODO: Replace with your mapper method
        finalResult.forEach(annualReviewMapper::insertFinalAssignment);
    }

    // 将 PotentialAssignment 转换为 FinalAssignment
    private FinalAssignment toAnnualReview(PotentialAssignment best, Room assignedRoom) {

        // 通过潜在的评审分配（PotentialAssignment）获取相应的信息
        PendingReviewDto reviewInfo = annualReviewService.getReviewInfoByPhdId(best.getPhdId());

        Teacher teacher1 = teacherMapper.selectById(best.getTeacher1Id());
        Teacher teacher2 = teacherMapper.selectById(best.getTeacher2Id());

        TimeSlot timeSlot = best.getTimeSlot();

        // 创建 FinalAssignment 对象并返回
        return new FinalAssignment(reviewInfo, teacher1, teacher2, assignedRoom, timeSlot);
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

                    if (skillScore == 0){
                        continue;
                    }

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

        // 返回所有的潜在评审分配方案
        return pool;
    }

    private PotentialAssignment selectBest(List<PotentialAssignment> pool, Map<Integer, Integer> workload) {

        if (pool == null || pool.isEmpty()) {
            return null;
        }
        return pool.stream()
                .max(Comparator.comparingDouble(p -> calculateFinalScore(p, workload)))
                .orElse(null);
        // 遍历 pool 中的每一个元素，使用 calculateFinalScore 方法为每个元素计算一个分数，然后找出这些元素中分数最高的那个元素。
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
        if (busySlots == null ||busySlots.isEmpty()){
            return true;
        }
        return busySlots.stream().noneMatch(busySlot -> busySlot.overlaps(targetSlot));


        // TODO: 根据 TimeSlot 比较逻辑判断冲突
    }

    private void updateBusy(Map<String, Set<TimeSlot>> busyMap, PotentialAssignment p, Room room) {
        // TODO: 将 slot 写入 teacher1, teacher2 和 room 的 busyMap
        TimeSlot newBusySlot = p.getTimeSlot();
        System.out.println(String.format("  > 更新日程图: 锁定 %s, 老师 %d, 老师 %d, 房间 %d",
                newBusySlot, p.getTeacher1Id(), p.getTeacher2Id(), room.getId()));

        // 为两位老师和一间会议室添加相同的繁忙时间段
        String teacher1Key = "teacher-" + p.getTeacher1Id();
        String teacher2Key = "teacher-" + p.getTeacher2Id();
        String roomKey = "room-" + room.getId();

        busyMap.computeIfAbsent(teacher1Key, k -> new HashSet<>()).add(newBusySlot);
        busyMap.computeIfAbsent(teacher2Key, k -> new HashSet<>()).add(newBusySlot);
        busyMap.computeIfAbsent(roomKey, k -> new HashSet<>()).add(newBusySlot);
        //
    }

    private void updateWorkload(Map<Integer, Integer> workload, PotentialAssignment p) {
        workload.put(p.getTeacher1Id(), workload.get(p.getTeacher1Id()) + 1);
        workload.put(p.getTeacher2Id(), workload.get(p.getTeacher2Id()) + 1);
    }

    //在排好一名学生后，移除冲突
    private List<PotentialAssignment> removeConflicts(List<PotentialAssignment> pool, PotentialAssignment used, Room room) {
        // 获取刚刚被占用的资源和时间
        int scheduledPhdId = used.getPhdId();
        int scheduledT1Id = used.getTeacher1Id();
        int scheduledT2Id = used.getTeacher2Id();
        int scheduledRoomId = room.getId();
        TimeSlot scheduledSlot = used.getTimeSlot();

        return pool.stream()
                .filter(p -> {
                    // 不能是刚刚已被安排的学生的其他方案
                    if (p.getPhdId() == scheduledPhdId) {
                        return false;
                    }
                    // 不能是在同一时间使用T1, T2或Room
                    if (p.getTimeSlot().overlaps(scheduledSlot)) {
                        if (p.getTeacher1Id() == scheduledT1Id || p.getTeacher2Id() == scheduledT1Id ||
                                p.getTeacher1Id() == scheduledT2Id || p.getTeacher2Id() == scheduledT2Id) {
                            return false;
                        }
                        //待添加roomId
                        //if (p.getRoomId() == scheduledRoomId) { return false; }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<TimeSlot> findCommonTimeSlots(List<AvailableTime> list1, List<AvailableTime> list2) {
        List<TimeSlot> commonSlots = new ArrayList<>();
        Set<TimeSlot> slot1 = list1.stream()
                .map(at1 -> new TimeSlot(at1.getStartTime(), at1.getEndTime()))
                .collect(Collectors.toSet());

        for(AvailableTime at2 : list2){
            TimeSlot slot2 = new TimeSlot(at2.getStartTime(), at2.getEndTime());
            if(slot1.contains(slot2)){
                commonSlots.add(slot2);
            }
        }

        return commonSlots;

    }

    private double calculateFinalScore(PotentialAssignment p, Map<Integer, Integer> workload){
        final double W_SKILL = 10.0;
        final double W_WORKLOAD = 5.0;
        // final double W_CONTINUITY = 3.0; // 时间连续性奖励权重 (此项较复杂，可后续添加)

        // 1. 获取技能分
        double skillScore = p.getSkillScore();

        // 2. 计算工作量惩罚
        double workloadPenalty = workload.get(p.getTeacher1Id()) + workload.get(p.getTeacher2Id());

        // 3. 计算时间连续性奖励
        // double continuityBonus = calculateContinuityBonus(p, finalResult);
        double continuityBonus = 0.0;

        // 返回最终加权分数
        return (W_SKILL * skillScore) - (W_WORKLOAD * workloadPenalty);
    }


}

