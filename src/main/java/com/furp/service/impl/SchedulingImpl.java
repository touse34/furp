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

import java.time.Duration;
import java.time.LocalDateTime;
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
    @Autowired private AvailableTimeService availableTimeService;


    public void autoSchedule() {
        List<PendingReviewDto> pendingReviews = annualReviewService.getPendingReviews();
        List<Integer> pendingPhdIds = pendingReviews.stream()
                .map(PendingReviewDto::getPhdId)
                .collect(Collectors.toList());   //获取等待分配的phdId
        List<PendingReviewDto> pendingReviewsCopy = new ArrayList<>(pendingReviews);

        List<Teacher> teachers = teacherService.findAllTeacher();
        List<Room> allRooms = roomMapper.selectAllRooms();

        // 检查是否有足够的房间
        if (allRooms.isEmpty()) {
            System.out.println("房间资源不足");
            return;  // 没有房间则提前退出
        }

        // autoSchedule() 顶部（初始化变量处）添加
        Map<Integer, LocalDateTime> roomLastEnd = new HashMap<>();

        Map<Integer, Integer> teacherWorkload = initWorkloadMap(teachers);
        Map<String, Set<TimeSlot>> busyMap = loadBusySlots(); //busymap:一个资源名对应一个timeslot的set集合
        Set<Integer> usedRooms = new HashSet<>();

        List<PotentialAssignment> pool = generatePotentialAssignments(pendingReviews, teachers, busyMap); // 方案池
        List<FinalAssignment> finalResult = new ArrayList<>();

        while (!pendingPhdIds.isEmpty() && !pool.isEmpty()) {
            PotentialAssignment best = selectBest(pool, teacherWorkload, busyMap);
            //Room assignedRoom = allocateRoom(best.getTimeSlot(), usedRooms, allRooms, busyMap);
            Room assignedRoom = allocateRoom(best.getTimeSlot(),
                    usedRooms,
                    allRooms,
                    busyMap,
                    roomLastEnd);
            if (assignedRoom == null) {
                // 房间不足，跳过此任务并输出提示信息
                System.out.println("房间资源不足,跳过学生" + best.getPhdId());
                System.out.println("还有"+pool.size());
                pool.remove(best);
                continue;
            }


            Teacher t1 = teachers.stream()
                    .filter(t -> t.getId().equals(best.getTeacher1Id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("致命错误: 在allTeachers列表中找不到ID为 " + best.getTeacher1Id() + " 的老师"));

            Teacher t2 = teachers.stream()
                    .filter(t -> t.getId().equals(best.getTeacher2Id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("致命错误: 在allTeachers列表中找不到ID为 " + best.getTeacher2Id() + " 的老师"));

// 2. 从预加载的 pendingReviewsCopy 列表中查找学生评审信息 (这也是内存操作，极快)
            PendingReviewDto scheduledReviewInfo = pendingReviewsCopy.stream()
                    .filter(r -> r.getPhdId().equals(best.getPhdId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("致命错误: 在待办列表中找不到phdId为 " + best.getPhdId() + " 的评审任务"));
            finalResult.add(new FinalAssignment(scheduledReviewInfo, t1, t2, assignedRoom, best.getTimeSlot()));




            updateBusy(busyMap, best, assignedRoom,roomLastEnd);
            updateWorkload(teacherWorkload, best);
            usedRooms.add(assignedRoom.getId());

            pendingPhdIds.removeIf(s -> s.equals(best.getPhdId()));
            pool = removeConflicts(pool, best, assignedRoom);
        }



        System.out.println("\n==============================================");
        System.out.println("          最终排程结果预览");
        System.out.println("==============================================");

        if (finalResult.isEmpty()) {
            System.out.println("未能生成任何有效的排程。");
        } else {
            System.out.println(String.format("成功生成 %d 条排程记录：", finalResult.size()));
            int count = 1;

            // 遍历每一个最终确定的排程方案
            for (FinalAssignment assignment : finalResult) {
                // 使用 String.format 来构建一个格式优美的字符串
                String output = String.format(
                        "%d. 学生: %-20s | 时间: %s - %s | 地点: %s | 评审员: %s, %s",
                        count++,
                        assignment.getReviewInfo().getStudentName(), // 学生姓名
                        assignment.getTimeSlot().getStartTime(), // 开始时间
                        assignment.getTimeSlot().getEndTime(),   // 结束时间
                        assignment.getRoom().getLocation(), // 房间名
                        assignment.getTeacher1().getName(), // 老师1姓名
                        assignment.getTeacher2().getName()  // 老师2姓名
                );
                System.out.println(output);
            }
        }
        System.out.println("==============================================\n");

    }

    // 将 PotentialAssignment 转换为 FinalAssignment


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
                String teacherKey = "teacher-" + schedule.getTeacherId();
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

        // 1. 一次性获取所有老师的所有技能，并存入Map
        // Key: teacherId, Value: Set of skillIds
        Map<Integer, Set<Integer>> allTeacherSkills = teacherSkillService.findAllTeacherSkillsAsMap();

        // 2. 一次性获取所有老师的所有意愿时间，并存入Map
        // Key: teacherId, Value: List of AvailableTime
        Map<Integer, List<AvailableTime>> allTeacherAvailabilities = availableTimeService.findAllAsMap();

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

                    Set<Integer> t1Skill = allTeacherSkills.get(t1.getId());
                    Set<Integer> t2Skill = allTeacherSkills.get(t2.getId());

                    long t1MatchCount = t1Skill.stream().filter(phdSkills::contains).count();
                    long t2MatchCount = t2Skill.stream().filter(phdSkills::contains).count();
                    int skillScore = (int) (t1MatchCount + t2MatchCount);

                    if (skillScore == 0){
                        continue;
                    }

                    List<AvailableTime> t1AvailableTime = allTeacherAvailabilities.get(t1.getId());
                    List<AvailableTime> t2AvailableTime = allTeacherAvailabilities.get(t2.getId());

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
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        System.out.println("共生成"+pool.size()+"总可行方案");
        // 返回所有的潜在评审分配方案
        return pool;
    }

    private PotentialAssignment selectBest(List<PotentialAssignment> pool, Map<Integer, Integer> workload, Map<String, Set<TimeSlot>> busyMap) {

        if (pool == null || pool.isEmpty()) {
            return null;
        }
        return pool.stream()
                .max(Comparator.comparingDouble(p -> calculateFinalScore(p, workload,busyMap)))
                .orElse(null);
        // 遍历 pool 中的每一个元素，使用 calculateFinalScore 方法为每个元素计算一个分数，然后找出这些元素中分数最高的那个元素。
    }

    private Room allocateRoom(TimeSlot slot, Set<Integer> usedRooms, List<Room> allRooms, Map<String, Set<TimeSlot>> busyMap,Map<Integer, LocalDateTime> roomLastEnd) {

        // --- 新增：方法入口提示 ---
        System.out.println(String.format("  -> [分配房间] 正在为时间段 %s 寻找可用房间...", slot));

        // --- 第一部分：优先重用已占用的房间 ---
        if (!usedRooms.isEmpty()) {
            System.out.println("    - 阶段1: 尝试重用已占用的房间...");
            for (Integer id : usedRooms) {
                // --- 新增：尝试重用的提示 ---
                System.out.println(String.format("      - 检查已用房间 ID: %d...", id));
                if (isFree("room-" + id, slot, busyMap)) {
                    // --- 新增：重用成功的提示 ---
                    System.out.println(String.format("      - [成功] 房间 %d 可用，将重用此房间。", id));
                    return allRooms.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
                }
            }
            // --- 新增：重用失败的提示 ---
            System.out.println("    - 阶段1结束: 所有已占用的房间在该时段都不可用。");
        }

        // --- 第二部分：启用一个新房间 ---
        System.out.println("    - 阶段2: 尝试寻找一个全新的空闲房间...");
        for (Room r : allRooms) {
            // --- 新增：检查新房间的提示 ---
            // System.out.println(String.format("      - 检查新房间 '%s' (ID: %d)...", r.getName(), r.getId()));

            if (!usedRooms.contains(r.getId())) { // 条件1：必须是还未被使用过的新房间
                if (isFree("room-" + r.getId(), slot, busyMap)) { // 条件2：这个新房间在该时段确实空闲
                    // --- 新增：启用新房间成功的提示 ---
                    System.out.println(String.format("      - [成功] 新房间 '%s' (ID: %d) 可用，将启用此房间。", r.getLocation(), r.getId()));
                    return r;
                }
            }
        }

        // ① 尝试找“上一次刚好连着结束”的房间
        Optional<Integer> bestFit = usedRooms.stream()
                .filter(id -> isFree("room-" + id, slot, busyMap))
                .filter(id ->  slot.getStartTime().equals(roomLastEnd.get(id)))   // 无缝衔接
                .findFirst();
        if (bestFit.isPresent()) {
            return allRooms.stream()
                    .filter(r -> r.getId().equals(bestFit.get()))
                    .findFirst()
                    .orElse(null);
        }

        // ② 如果没有无缝房，再找“有空闲但不冲突”的已用房，按 idle 长度升序
        Optional<Integer> closeFit = usedRooms.stream()
                .filter(id -> isFree("room-" + id, slot, busyMap))
                .sorted(Comparator.comparing(id ->
                        Duration.between(roomLastEnd.get(id), slot.getStartTime()).abs()))
                .findFirst();
        if (closeFit.isPresent()) {
            return allRooms.stream()
                    .filter(r -> r.getId().equals(closeFit.get()))
                    .findFirst()
                    .orElse(null);
        }

        // ③ 仍然不行就启用全新房
        for (Room r : allRooms) {
            if (!usedRooms.contains(r.getId()) &&
                    isFree("room-" + r.getId(), slot, busyMap)) {
                return r;
            }
        }

        // --- 第三部分：处理无可用房间的情况 ---
        // --- 新增：分配失败的最终提示 ---
        System.out.println("  -> [分配失败] 找不到任何可用的房间。");
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

    private void updateBusy(Map<String, Set<TimeSlot>> busyMap, PotentialAssignment p, Room room, Map<Integer, LocalDateTime> roomLastEnd) {
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
        // 新增：记录该房间最新结束时间
        roomLastEnd.put(room.getId(), newBusySlot.getEndTime());
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

    private static final double W_CONTINUITY = 3.0;   // 连续性奖励权重，可自行调大/调小

    private double calculateFinalScore(PotentialAssignment p, Map<Integer, Integer> workload, Map<String, Set<TimeSlot>> busyMap){
        final double W_SKILL = 10.0;
        final double W_WORKLOAD = 5.0;
        // final double W_CONTINUITY = 3.0; // 时间连续性奖励权重 (此项较复杂，可后续添加)

        // 1. 获取技能分
        double skillScore = p.getSkillScore();

        // 2. 计算工作量惩罚
        double workloadPenalty = workload.get(p.getTeacher1Id()) + workload.get(p.getTeacher2Id());

        // 3. 计算时间连续性奖励
        // double continuityBonus = calculateContinuityBonus(p, finalResult);
        //double continuityBonus = 0.0;

        double continuityBonus = calculateContinuityBonus(p, busyMap);   //  新增


        // 返回最终加权分数
        //return (W_SKILL * skillScore) - (W_WORKLOAD * workloadPenalty);
        return (W_SKILL * skillScore)
                - (W_WORKLOAD * workloadPenalty)
                + (W_CONTINUITY * continuityBonus);          //  把连贯分加进去
    }

    /**
     * 若老师在 busyMap 里已有时间段与 p 的 slot 紧邻（前后相差 ≤15 分钟），
     * 就奖励 1 分；两位老师最多各加 1 分，返回 0~2 之间的值。
     */
    private double calculateContinuityBonus(PotentialAssignment p,
                                            Map<String, Set<TimeSlot>> busyMap) {

        TimeSlot cur = p.getTimeSlot();
        double bonus = 0.0;

        for (int tid : List.of(p.getTeacher1Id(), p.getTeacher2Id())) {
            Set<TimeSlot> busy = busyMap.getOrDefault("teacher-" + tid, Set.of());

            boolean hasAdjacent = busy.stream().anyMatch(b -> {
                long gapFront = Duration.between(b.getEndTime(), cur.getStartTime()).toMinutes();
                long gapBack  = Duration.between(cur.getEndTime(), b.getStartTime()).toMinutes();
                return (gapFront >= 0 && gapFront <= 15)   // 前后相差不超过 15 分钟
                        || (gapBack  >= 0 && gapBack  <= 15);
            });

            if (hasAdjacent) bonus += 1.0;
        }
        return bonus;   // 0, 1, or 2
    }



}

