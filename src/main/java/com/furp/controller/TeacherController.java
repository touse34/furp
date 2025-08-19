package com.furp.controller;

import com.furp.DTO.TeacherProfileDTO;
import com.furp.DTO.TeacherTimeSelectionDTO;
import com.furp.VO.AcademicTermVO;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.Result;
import com.furp.service.TeacherProfileService;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TimeSlotsService timeSlotsService;
    @Autowired
    private TeacherProfileService teacherProfileService;

    /**
     *  2.1 获取当前时间配置
     * @param year
     * @return
     */
    @GetMapping("/time-config")
    public Result<List<TimeConfigVO>> getTimeConfig(String year) {
        List<TimeConfigVO> list = timeSlotsService.getAvailableTimeSlots(year);
        return Result.success(list);
    }

    @GetMapping("/academic-terms")
    public Result<List<AcademicTermVO>> getAvailableAcademicTerms() {
            List<AcademicTermVO> terms = timeSlotsService.getAvailableAcademicTerms();
        return Result.success(terms);
    }


    /**
     * ### 3.1 获取教师时间选择
     * @param academicYear
     * @return
     */
    @GetMapping("/teacher/time-selection")
    public Result<List<TimeConfigVO>> getTeacherTimeSelection(@RequestAttribute("teacherId") Integer teacherId,
                                                              @RequestParam String academicYear) {
        List<TimeConfigVO> list = timeSlotsService.getTeacherAvailableTimeSlots(academicYear, teacherId);

        return Result.success(list);
    }

    /**
     * ### 3.2 保存教师时间选择
     * @param selectedTime
     * @return
     */
    @PutMapping("/teacher/time-selection-confirm")
    public Result updateTeacherTimeSelection(@RequestAttribute("teacherId") Integer teacherId,
                                              @RequestBody TeacherTimeSelectionDTO selectedTime) {
        int updatedCount = timeSlotsService.updateTeacherTimeSelection(teacherId, selectedTime);
        return Result.success("成功更新了" + updatedCount + "个可用时间段。");
    }

    /**
     * 获取教师基本信息
     * @param userId
     * @return
     */

    @GetMapping("/v1/teacher/profile")
    public Result<TeacherProfileDTO> getTeacherProfile(@RequestAttribute("currentUserId") Integer userId) {
        System.out.println("当前登录者 userId = " + userId);

        TeacherProfileDTO teacherProfileDTO = teacherProfileService.getById(userId);

        if (teacherProfileDTO == null) {
            return Result.success(null);  // 或 Result.error("未绑定PhD信息")
        }

        return Result.success(teacherProfileDTO);
    }

    /**
     * 修改教师信息
     * @param
     * @return
     */
    @PutMapping("/v1/teacher/profile")
    public Result updateTeacherProfile(@RequestBody TeacherProfileDTO teacherProfileDTO,
                                       @RequestAttribute("currentUserId") Integer currentUserId) {
        System.out.println("修改教师信息：" + teacherProfileDTO);
        System.out.println("当前用户ID：" + currentUserId);

        // 设置userId为当前用户ID（从JWT获取）
        teacherProfileDTO.setUserId(String.valueOf(currentUserId));

        teacherProfileService.update(teacherProfileDTO);
        return Result.success();
    }


}
