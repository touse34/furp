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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        // ---------- 1. 准备模拟数据 ----------
        PendingReviewDto phd = new PendingReviewDto();
        phd.setPhdId(1);
        phd.setStudentName("张三");

        Teacher t1 = new Teacher();
        t1.setId(101);
        Teacher t2 = new Teacher();
        t2.setId(102);

        Room room = new Room();
        room.setId(201);

        LocalDateTime start = LocalDateTime.of(2025, 7, 10, 9, 0);
        LocalDateTime end   = start.plusMinutes(45);
        AvailableTime at    = new AvailableTime();
        at.setStartTime(start);
        at.setEndTime(end);

        // ---------- 2. stub 所有用到的依赖 ----------
        when(annualReviewService.getPendingReviews()).thenReturn(List.of(phd));
        when(teacherService.findAllTeacher()).thenReturn(List.of(t1, t2));
        when(teacherService.findEligibleAssessors(1)).thenReturn(List.of(t1, t2));

        when(roomMapper.selectAllRooms()).thenReturn(List.of(room));
        when(phdSkillService.findPhdSkillsById(1)).thenReturn(Set.of(1, 2));
        when(teacherSkillService.findTeacherSkillsById(101)).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(102)).thenReturn(Set.of(2));
        when(availableTimeMapper.findByTeacherId(101)).thenReturn(List.of(at));
        when(availableTimeMapper.findByTeacherId(102)).thenReturn(List.of(at));
        when(schedulesMapper.findAllFutureSchedules()).thenReturn(List.of());
        when(annualReviewService.getReviewInfoByPhdId(1)).thenReturn(phd);

        // **关键：补上 selectById 的返回值，避免 NPE**
        when(teacherMapper.selectById(101)).thenReturn(t1);
        when(teacherMapper.selectById(102)).thenReturn(t2);

        // ---------- 3. 执行待测方法 ----------
        scheduling.autoSchedule();

        // ---------- 4. 捕获并断言插入的数据 ----------
        ArgumentCaptor<SchedulingImpl.FinalAssignment> captor =
                ArgumentCaptor.forClass(SchedulingImpl.FinalAssignment.class);

        // **把验证和捕获合并成一次，避免重复 verify**
        verify(annualReviewMapper, times(1)).insert(captor.capture());

        SchedulingImpl.FinalAssignment result = captor.getValue();
        assertNotNull(result);                         // 保险起见
        assertEquals(101, result.getTeacher1().getId());
        assertEquals(102, result.getTeacher2().getId());
        assertEquals(201, result.getRoom().getId());
        assertEquals(start, result.getTimeSlot().getStartTime());
        assertEquals(end,   result.getTimeSlot().getEndTime());
    }

}
