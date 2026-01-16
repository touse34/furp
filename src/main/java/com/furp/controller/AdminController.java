package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.furp.DTO.*;
import com.furp.VO.PendingResearchAreaVO;
import com.furp.VO.ReviewResultVO;
import com.furp.VO.UserAddResponseVO;
import com.furp.entity.Result;
import com.furp.response.PageResult;
import com.furp.service.StudentAddService;
import com.furp.service.TimeSlotsService;
import com.furp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private StudentAddService studentAddService;


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

    /*
    5.3 添加研究方向
     */
    @PostMapping("/research-areas")
    public Result<Void> addResearchArea(@RequestBody ResearchAreaDetail researchAreaDetail) {
        log.info("添加研究方向: {}", researchAreaDetail);
        userService.addResearchArea(researchAreaDetail);
        return Result.success("添加成功");
    }

    /*
    5.4 更新研究方向名字
     */
    @PutMapping("/research-areas/{areaId}")
    public Result<Void> updateResearchArea(@PathVariable Integer areaId, @RequestBody ResearchAreaUpdateDTO updateDTO) {
        log.info("更新研究方向 {}, 数据: {}", areaId, updateDTO);
        userService.updateResearchArea(areaId, updateDTO);
        return Result.success("研究方向更新成功");
    }

    /*
    5.5 删除研究方向
     */
    @DeleteMapping("/research-areas/{areaId}")
    public Result<Void> deleteResearchArea(@PathVariable Integer areaId) {
        log.info("删除研究方向: {}", areaId);
        userService.deleteResearchArea(areaId);
        return Result.success("研究方向删除成功");
    }

    /*
    审核研究方向
     */
    @PutMapping("/research-areas/{areaId}/review")
    public Result<ReviewResultVO> reviewResearchArea(@PathVariable Integer areaId, @RequestBody ResearchAreaReviewDTO reviewDTO) {
        log.info("审核研究方向 {}, 操作: {}", areaId, reviewDTO);
        ReviewResultVO resultVO = userService.reviewResearchArea(areaId, reviewDTO);
        return Result.success("审核完成", resultVO);
    }


    /**
     * 1. Excel 批量导入接口
     * URL: POST /students/import
     * 参数: file (Form-Data)
     */
    @PostMapping("/import")
    public Result<Void> importStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        try {
            String msg = studentAddService.batchImportStudents(file);

            return Result.success(msg);
        } catch (Exception e) {
            e.printStackTrace();
            // 把具体的错误信息返回给前端，方便调试
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 2. 手动录入单个学生接口
     * URL: POST /students/add
     * 参数: JSON Body
     */
    @PostMapping("/add")
    public Result<Void> addStudent(@RequestBody StudentImportDTO studentDTO) {
        try {
            studentAddService.addOneStudent(studentDTO);
            return Result.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败: " + e.getMessage());
        }
    }







}
