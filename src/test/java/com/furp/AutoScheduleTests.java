package com.furp;

import com.furp.DTO.response.PendingReviewDto;
import com.furp.entity.*;
import com.furp.mapper.*;
import com.furp.service.*;
import com.furp.service.impl.SchedulingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

public class AutoScheduleTests {
    @InjectMocks
    private SchedulingImpl scheduling;

    @Mock private PhdMapper phdMapper;
    @Mock private TeacherMapper teacherMapper;
    @Mock private RoomMapper roomMapper;
    @Mock private AnnualReviewService annualReviewService;
    @Mock private TeacherService teacherService;
    @Mock private PhdSkillService phdSkillService;
    @Mock private TeacherSkillService teacherSkillService;
    @Mock private AvailableTimeMapper availableTimeMapper;
    @Mock private SchedulesMapper schedulesMapper;
    @Mock private AnnualReviewMapper annualReviewMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 @Mock 和 @InjectMocks
    }

    @Test
    void testAutoSchedule_SuccessCase() {
        // 模拟学生
        PendingReviewDto phd = new PendingReviewDto();
        phd.setPhdId(1);
        phd.setStudentName("张三");

        // 模拟评审老师
        Teacher t1 = new Teacher();
        t1.setId(101);
        Teacher t2 = new Teacher();
        t2.setId(102);

        // 模拟会议室
        Room room = new Room();
        room.setId(201);

        // 模拟时间
        LocalDateTime start = LocalDateTime.of(2025, 7, 10, 9, 0);
        LocalDateTime end = start.plusMinutes(45);
        AvailableTime at = new AvailableTime();
        at.setStartTime(start);
        at.setEndTime(end);

        // mock 返回值
        when(annualReviewService.getPendingReviews()).thenReturn(List.of(phd));
        when(teacherService.findAllTeacher()).thenReturn(List.of(t1, t2));
        when(teacherService.findEligibleAssessors(1)).thenReturn(List.of(t1, t2));  // ✅必须补上
        when(roomMapper.selectAllRooms()).thenReturn(List.of(room));
        when(phdSkillService.findPhdSkillsById(1)).thenReturn(Set.of(1, 2));
        when(teacherSkillService.findTeacherSkillsById(101)).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(102)).thenReturn(Set.of(2));
        when(availableTimeMapper.findByTeacherId(101)).thenReturn(List.of(at));
        when(availableTimeMapper.findByTeacherId(102)).thenReturn(List.of(at));
        when(schedulesMapper.findAllFutureSchedules()).thenReturn(List.of());

        when(annualReviewService.getReviewInfoByPhdId(1)).thenReturn(phd);

        // 调用方法
        scheduling.autoSchedule();

        // 验证是否调用 insert 方法
        verify(annualReviewMapper, times(1)).insert(Mockito.any(SchedulingImpl.FinalAssignment.class));



    }
}
