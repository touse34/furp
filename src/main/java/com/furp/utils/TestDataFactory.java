package com.furp.utils;

import com.furp.DTO.PendingReviewDto;
import com.furp.entity.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 为单元测试提供统一的 Mock 数据
 */
public class TestDataFactory {

    /* ---------- Teacher ---------- */
    public static List<Teacher> mockTeachers() {
        Teacher t1 = new Teacher(); t1.setId(1); t1.setName("张老师");
        Teacher t2 = new Teacher(); t2.setId(2); t2.setName("李老师");
        return List.of(t1, t2);
    }

    /* ---------- Teacher Skill Map ---------- */
    public static Map<Integer, Set<Integer>> mockTeacherSkillMap() {
        return Map.of(
                1, Set.of(1, 2),   // 张老师: 技能 1、2
                2, Set.of(2, 3)    // 李老师: 技能 2、3
        );
    }

    /* ---------- Teacher Available Time ---------- */
    public static Map<Integer, List<AvailableTime>> mockAvailableTimes() {
        LocalDateTime start = LocalDateTime.of(2025, 7, 20, 9, 0);
        LocalDateTime end   = start.plusMinutes(45);

        AvailableTime at = new AvailableTime();
        at.setStartTime(start);
        at.setEndTime(end);

        return Map.of(
                1, List.of(at),   // 张老师 9:00-9:45
                2, List.of(at)    // 李老师 9:00-9:45
        );
    }

    /* ---------- Pending PhD Reviews ---------- */
    public static List<PendingReviewDto> mockPhdReviews() {
        PendingReviewDto dto = new PendingReviewDto();
        dto.setPhdId(1001);
        dto.setStudentName("王同学");
        dto.setSupervisorId(999);   // 主导师不是 1、2，避免冲突
        return List.of(dto);
    }

    /* ---------- Eligible Assessors ---------- */
    public static List<Teacher> mockEligibleAssessors() {
        return mockTeachers();      // 直接用两位老师都合格
    }

    /* ---------- Rooms ---------- */
    public static List<Room> mockRooms() {
        Room r = new Room();
        r.setId(301);
        r.setLocation("综合楼 301");
        return List.of(r);
    }

    /* ---------- Existing Future Schedules ---------- */
    public static List<Schedules> mockSchedules() {
        return List.of();           // 为空 ⇒ 没有冲突
    }
}
