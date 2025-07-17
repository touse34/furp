package com.furp;

import com.furp.service.impl.SchedulingImpl;
import com.furp.service.*;
import com.furp.mapper.*;
import com.furp.utils.TestDataFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static com.furp.utils.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * 单元测试：验证在“资源充足”场景下自动排程能够成功生成结果
 */
@ExtendWith(MockitoExtension.class)
public class SchedulingImplTest {

    /* ========= 被测对象 ========= */
    @InjectMocks
    private SchedulingImpl schedulingService;

    /* ========= 依赖 Mock ========= */
    @Mock private TeacherService teacherService;
    @Mock private PhdSkillService phdSkillService;
    @Mock private TeacherSkillService teacherSkillService;
    @Mock private AvailableTimeService availableTimeService;
    @Mock private AnnualReviewService annualReviewService;
    @Mock private RoomMapper roomMapper;
    @Mock private SchedulesMapper schedulesMapper;
    /* 其余 Mapper/Service 如有注入要求也可 @Mock，这里省略未用到者 */

    /* ========= 测试用例 ========= */
    @Test
    public void testAutoSchedule_whenResourcesSufficient_shouldGenerateAssignments() {

        /* ---------- 1. 配置 Mock 行为 ---------- */
        when(teacherService.findAllTeacher()).thenReturn(mockTeachers());
        when(teacherService.findEligibleAssessors(anyInt())).thenReturn(mockEligibleAssessors());

        when(phdSkillService.findPhdSkillsById(anyInt())).thenReturn(Set.of(1, 2));
        when(teacherSkillService.findAllTeacherSkillsAsMap()).thenReturn(mockTeacherSkillMap());
        when(availableTimeService.findAllAsMap()).thenReturn(mockAvailableTimes());

        when(annualReviewService.getPendingReviews()).thenReturn(mockPhdReviews());
        when(roomMapper.selectAllRooms()).thenReturn(mockRooms());
        when(schedulesMapper.findAllFutureSchedules()).thenReturn(mockSchedules());

        /* ---------- 2. 捕获控制台输出 ---------- */
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try {
            /* ---------- 3. 调用被测方法 ---------- */
            schedulingService.autoSchedule();
        } finally {
            System.setOut(originalOut);  // 恢复
        }

        /* ---------- 4. 断言：输出包含“成功生成”字段 ---------- */
        String console = baos.toString(StandardCharsets.UTF_8);
        assertTrue(console.contains("成功生成"), "应当生成至少一条排程记录");
    }
}
