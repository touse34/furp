package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.furp.DTO.*;
import com.furp.VO.PendingResearchAreaVO;
import com.furp.VO.UserAddResponseVO;
import com.furp.entity.Result;
import com.furp.response.PageResult;
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

    /*
     4.2 添加用户
     */
    @PostMapping("/users")
    public Result<UserAddResponseVO> addUser(@RequestBody UserAddDTO userAddDTO) {
        log.info("添加用户, 参数: {}", userAddDTO);

        // 2. 调用 service 方法，并接收返回的 VO 对象
        UserAddResponseVO responseVO = userService.addUser(userAddDTO);

        // 3. 将 VO 对象放入 Result.success() 中返回
        return Result.success(responseVO);
    }

    /*
     4.3.1 禁用用户
     */
    @DeleteMapping("/users/disable/{userId}")
    public Result<Void> disableUser(@PathVariable Integer userId){
        log.info("禁用用户: {}", userId);
        userService.disableUserById(userId);
        return Result.success("用户删除成功");

    }

    /**
     4.3.2 启用用户
     */
    @PutMapping("/users/enable/{userId}")
    public Result<Void> enableUser(@PathVariable Integer userId){
        log.info("启用用户: {}", userId);
        userService.enableUser(userId);
        return Result.success("用户启用成功");

    }

    /*
    4.3删除用户
     */
    @DeleteMapping("/users/{userId}")
    public Result<Void> deleteUser(@PathVariable Integer userId){
        log.info("删除用户: {}", userId);
        userService.deleteUserById(userId);
        return Result.success("用户删除成功");
    }

    /*
    4.3更新用户信息 获取可选时间段
     */
    @PutMapping("/users/{userId}")
    public Result<Void> updateUser(@PathVariable Integer userId, @RequestBody UserAddDTO userAddDTO){
        log.info("更新用户: {}", userAddDTO);
        userService.updateUser(userId,userAddDTO);
        return Result.success("用户更新成功");
    }

    /*
    4.5 导入用户Excel
     */

    /*
    4.5 导入用户Excel
     */


    /*
    5.1 获取所有研究方向
     */
    @GetMapping("/research-areas")
    public Result<PageResult> researchAreas(ResearchAreaPageQueryDTO QueryDTO){
        // 1. 记录接收到的参数，便于调试
        log.info("分页查询用户列表, 参数: {}", QueryDTO);

        // 2. 调用 Service 层执行查询
        PageResult pageResult = userService.researchAreasQuery(QueryDTO);

        // 3. 封装结果并返回
        return Result.success(pageResult);

    /*
    5.1 获取所有研究方向
     */
    @GetMapping("/research-areas")
    public Result<PageResult> researchAreas(ResearchAreaPageQueryDTO QueryDTO){
        // 1. 记录接收到的参数，便于调试
        log.info("分页查询用户列表, 参数: {}", QueryDTO);

        // 2. 调用 Service 层执行查询
        PageResult pageResult = userService.researchAreasQuery(QueryDTO);

        // 3. 封装结果并返回
        return Result.success(pageResult);


    }

    }

    /*
    5.2 获取待审核研究方向
     */
    @GetMapping("/research-areas/pending")
    public Result<List<PendingResearchAreaVO>> getPending(PendingResearchAreaQueryDTO queryDTO) {
        log.info("查询待审核研究方向: {}", queryDTO);
        List<PendingResearchAreaVO> list = userService.getPending(queryDTO);
        return Result.success(list);
    }







}
