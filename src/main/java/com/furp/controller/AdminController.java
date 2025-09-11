package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.furp.DTO.DashboardStatsDTO;
import com.furp.DTO.TimeSlot;
import com.furp.DTO.UpdateTimeSlotsDTO;
import com.furp.DTO.UserPageQueryDTO;
import com.furp.entity.AvailableTime;
import com.furp.entity.Result;
import com.furp.mapper.TimeSlotsMapper;
import com.furp.response.PageResult;
import com.furp.service.AvailableTimeService;
import com.furp.service.TimeSlotsService;
import com.furp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/admin")
@SaCheckRole("admin")
public class AdminController {

    @Autowired
    private TimeSlotsService timeSlotsService;
    @Autowired
    private UserService userService;

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


    /*
    3.1 获取系统统计信息
     */
    @GetMapping("/dashboard/stats")
    public Result<DashboardStatsDTO> getDashboardStats() {
        System.out.println("getDashboardStats");
        DashboardStatsDTO statsDTO = userService.getStats();
        return Result.success(statsDTO);
    }

    /*
    4.1 获取用户列表
     */
    @GetMapping("/users")
    public Result<PageResult> pageQuery(UserPageQueryDTO userPageQueryDTO){
        // 1. 记录接收到的参数，便于调试
        log.info("分页查询用户列表, 参数: {}", userPageQueryDTO);

        // 2. 调用 Service 层执行查询
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);

        // 3. 封装结果并返回
        return Result.success(pageResult);

    }












}
