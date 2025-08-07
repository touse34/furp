package com.furp.controller;

import com.furp.DTO.TeacherTimeSelectionDTO;
import com.furp.VO.AcademicTermVO;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.Result;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    private TimeSlotsService timeSlotsService;

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



    @GetMapping("/teacher/time-selection")
    public Result<List<TimeConfigVO>> getTeacherTimeSelection(@RequestAttribute("teacherId") Integer teacherId,
                                                              @RequestParam String academicYear) {
        List<TimeConfigVO> list = timeSlotsService.getTeacherAvailableTimeSlots("year", 9L);

        return Result.success(list);
    }

    @PutMapping("/teacher/time-selection-confirm")
    public Result updateTeacherTimeSelection(@RequestAttribute("teacherId") Integer teacherId,
                                              @RequestBody TeacherTimeSelectionDTO selectedTime) {
        int updatedCount = timeSlotsService.updateTeacherTimeSelection(teacherId, selectedTime);
        return Result.success("成功更新了" + updatedCount + "个可用时间段。");
    }

}
