package com.furp.controller;

import com.furp.VO.TimeConfigVO;
import com.furp.entity.Result;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<List<TimeConfigVO>> getTimeConfig(Integer year) {
        List<TimeConfigVO> list = timeSlotsService.getAvailableTimeSlots(year);
        return Result.success(list);
    }
}
