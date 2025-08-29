package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.furp.DTO.TimeSlot;
import com.furp.DTO.UpdateTimeSlotsDTO;
import com.furp.entity.AvailableTime;
import com.furp.entity.Result;
import com.furp.mapper.TimeSlotsMapper;
import com.furp.service.AvailableTimeService;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SaCheckRole("admin")
public class AdminController {

    @Autowired
    private TimeSlotsService timeSlotsService;

    /**2.3 更新日期配置（管理员）
     *
     * @param updateTimeSlots
     * @return
     */
    @PostMapping("/date-configs")
    public Result upDateDateConfigs(@RequestBody UpdateTimeSlotsDTO updateTimeSlots) {
        String year = updateTimeSlots.getAcademicYear();
        List<TimeSlot> slots= updateTimeSlots.getSlots();
        int updatedCount = timeSlotsService.updateDateConfigs(year, slots);
        return Result.success("成功创建/更新了" + updatedCount + "个可用时间段。");
    }
}
