package com.furp;

import com.furp.DTO.FinalAssignment;
import com.furp.DTO.PendingReviewDto;
import com.furp.entity.*;
import com.furp.mapper.*;
import com.furp.service.*;
import com.furp.service.impl.SchedulingImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    // ---------- 捕获 System.out（可选） ----------
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 @Mock 和 @InjectMocks

    }

    @BeforeEach
    void redirectStdout() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStdout() {
        System.setOut(originalOut);
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
        ArgumentCaptor<FinalAssignment> captor =
                ArgumentCaptor.forClass(FinalAssignment.class);

        // **把验证和捕获合并成一次，避免重复 verify**
        verify(annualReviewMapper, times(1)).insertFinalAssignment(captor.capture());

        FinalAssignment result = captor.getValue();
        assertNotNull(result);                         // 保险起见
        assertEquals(101, result.getTeacher1().getId());
        assertEquals(102, result.getTeacher2().getId());
        assertEquals(201, result.getRoom().getId());
        assertEquals(start, result.getTimeSlot().getStartTime());
        assertEquals(end,   result.getTimeSlot().getEndTime());
    }

    @Test
    void testAutoSchedule_RoomNotEnough() {
        // 时间段
        LocalDateTime start = LocalDateTime.of(2025, 7, 10, 9, 0);
        LocalDateTime end = start.plusMinutes(45);

        // 学生
        PendingReviewDto phd1 = new PendingReviewDto(); phd1.setPhdId(1); phd1.setStudentName("张三");
        PendingReviewDto phd2 = new PendingReviewDto(); phd2.setPhdId(2); phd2.setStudentName("李四");

        // 老师
        Teacher t1 = new Teacher(); t1.setId(101);
        Teacher t2 = new Teacher(); t2.setId(102);

        // 房间
        Room room = new Room(); room.setId(201);

        // 时间段
        AvailableTime at = new AvailableTime();
        at.setStartTime(start);
        at.setEndTime(end);

        // 房间已被占用（mock进 schedule -> busyMap）
        Schedules roomUsed = new Schedules();
        roomUsed.setRoomId(201);
        roomUsed.setStartTime(start);
        roomUsed.setEndTime(end);

        // mock 数据
        when(annualReviewService.getPendingReviews()).thenReturn(List.of(phd1, phd2));
        when(teacherService.findAllTeacher()).thenReturn(List.of(t1, t2));
        when(teacherService.findEligibleAssessors(anyInt())).thenReturn(List.of(t1, t2));
        when(roomMapper.selectAllRooms()).thenReturn(List.of(room));
        when(phdSkillService.findPhdSkillsById(anyInt())).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(101)).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(102)).thenReturn(Set.of(1));
        when(availableTimeMapper.findByTeacherId(101)).thenReturn(List.of(at));
        when(availableTimeMapper.findByTeacherId(102)).thenReturn(List.of(at));
        when(schedulesMapper.findAllFutureSchedules()).thenReturn(List.of(roomUsed));
        when(annualReviewService.getReviewInfoByPhdId(anyInt())).thenReturn(phd1);
        when(teacherMapper.selectById(101)).thenReturn(t1);
        when(teacherMapper.selectById(102)).thenReturn(t2);

        // 调用调度
        scheduling.autoSchedule();

        // 断言 insert 只执行一次（第二个学生失败）
        //verify(annualReviewMapper, times(1)).insert(any());
        verify(annualReviewMapper, times(1)).insertFinalAssignment(any(FinalAssignment.class));

        // 输出调试信息
        String console = outContent.toString();
        System.out.println("Captured Output: " + console);

        // 断言打印内容包含“房间资源不足”
        assertTrue(console.contains("房间资源不足"), "应提示房间资源不足");



    }
    @Test
    void testAutoSchedule_RoomNotEnough1() {
        // 1. 准备数据 ---------------------------------------------------------
        PendingReviewDto phd1 = new PendingReviewDto();
        phd1.setPhdId(1);
        phd1.setStudentName("张三");

        PendingReviewDto phd2 = new PendingReviewDto();
        phd2.setPhdId(2);
        phd2.setStudentName("李四");

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

        // 2. stub 依赖 ---------------------------------------------------------
        when(annualReviewService.getPendingReviews()).thenReturn(List.of(phd1, phd2));
        when(teacherService.findAllTeacher()).thenReturn(List.of(t1, t2));
        when(teacherService.findEligibleAssessors(1)).thenReturn(List.of(t1, t2));
        when(teacherService.findEligibleAssessors(2)).thenReturn(List.of(t1, t2));

        when(roomMapper.selectAllRooms()).thenReturn(List.of(room));  // 只有 1 间房间
        when(phdSkillService.findPhdSkillsById(1)).thenReturn(Set.of(1));
        when(phdSkillService.findPhdSkillsById(2)).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(101)).thenReturn(Set.of(1));
        when(teacherSkillService.findTeacherSkillsById(102)).thenReturn(Set.of(2));
        when(availableTimeMapper.findByTeacherId(101)).thenReturn(List.of(at));
        when(availableTimeMapper.findByTeacherId(102)).thenReturn(List.of(at));

        // 模拟房间已占用
        Schedules roomUsed = new Schedules();
        roomUsed.setRoomId(201);
        roomUsed.setStartTime(start);
        roomUsed.setEndTime(end);

        when(schedulesMapper.findAllFutureSchedules()).thenReturn(List.of(roomUsed));

        when(annualReviewService.getReviewInfoByPhdId(1)).thenReturn(phd1);
        when(annualReviewService.getReviewInfoByPhdId(2)).thenReturn(phd2);

        // 3. 执行方法 ---------------------------------------------------------
        scheduling.autoSchedule();

        // 4. 验证：insert 被调用，且没有房间冲突 -----------------------------------------
        verify(annualReviewMapper, times(1)).insertFinalAssignment(any(FinalAssignment.class));

        // 5. 调试：打印捕获的日志输出 ----------------------------------------
        System.out.println("Captured Output: " + outContent.toString());  // 调试用，查看输出内容

        // 6. 断言：输出内容应包含 "房间资源不足" ----------------------------------------
        assertTrue(outContent.toString().contains("房间资源不足"), "应提示房间资源不足");
    }


}
