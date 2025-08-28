package com.furp.controller;

import com.furp.DTO.*;
import com.furp.VO.AcademicTermVO;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.Result;
import com.furp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TimeSlotsService timeSlotsService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherResearchAreasResponseService teacherResearchAreasResponseService;
    @Autowired
    private TeacherSkillService teacherSkillService;

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
    @GetMapping("/user/time-selection")
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
    @PutMapping("/user/time-selection-confirm")
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

    @GetMapping("/profile")
    public Result<TeacherProfileDTO> getTeacherProfile(@RequestAttribute("currentUserId") Integer userId) {
        System.out.println("当前登录者 userId = " + userId);

        TeacherProfileDTO teacherProfileDTO = teacherProfileService.getById(userId);

        if (teacherProfileDTO == null) {
            return Result.success(null);  // 或 Result.error("未绑定PhD信息")
        }

        return Result.success(teacherProfileDTO);
    }

    /**
     * 修改教师姓名，这个功能已删除
     * @param
     * @return
     */
    @PutMapping("/profile")
    public Result updateTeacherProfile(@RequestBody TeacherProfileDTO teacherProfileDTO,
                                       @RequestAttribute("currentUserId") Integer currentUserId) {
        System.out.println("修改教师信息：" + teacherProfileDTO);
        System.out.println("当前用户ID：" + currentUserId);

        // 设置userId为当前用户ID（从JWT获取）
        teacherProfileDTO.setUserId(currentUserId);

        teacherProfileService.update(teacherProfileDTO);
        return Result.success();
    }

    /**
     * 4.1获取用户评审任务
     * @param
     * @return
     */
    @GetMapping("/user/review-tasks")
    public Result<List<ReviewInfoVo>> getReviewTasks(@RequestAttribute("teacherId") Integer teacherId) {

        List<ReviewInfoVo> reviewInfo = teacherService.findReviewScheduleByTeacherId(teacherId);

        return Result.success(reviewInfo);
    }


    /**
     * 7.1获取教师研究领域
     * @param teacherId
     * @return
     */
    @GetMapping("/research-areas")
    public Result<TeacherResearchAreasResponseDTO> getTeacherResearchAreas(@RequestAttribute("teacherId") Integer teacherId) {
        System.out.println("获取教师研究方向,教师Id: "+ teacherId);

        TeacherResearchAreasResponseDTO responseDTO = teacherResearchAreasResponseService.getTeacherResearchAreas(teacherId);

        return Result.success(responseDTO);
    }



    /*
    7.2给某一个老师增添他的研究方向, 一次只能添加一个
     */
    @PostMapping("/research-areas/no-use")
    public Result<ResearchAreaDetail> addTeacherResearchArea(@RequestAttribute("teacherId") Integer teacherId,
                                                             @RequestBody ResearchAreaDetail researchAreaDetail){

        System.out.println("获取教师研究方向,教师Id: "+ teacherId);

        ResearchAreaDetail insertedResearchArea = teacherResearchAreasResponseService.addResearchArea(teacherId,researchAreaDetail);

        return Result.success(insertedResearchArea);
    }

    /*
    7.2.1 一次能加很多个研究方向
     */
    @PostMapping("/research-areas")
    public Result addResearchAreas(@RequestAttribute("teacherId") Integer teacherId, @RequestBody ResearchAreaRequest request) {
        // 假设通过某种方式（如 Sa-Token, Spring Security）获取当前登录的教师ID
        // 例如: Integer teacherId = StpUtil.getLoginIdAsInt();


        List<ResearchAreaDetail> insertedAreas = teacherResearchAreasResponseService.addResearchAreasForTeacher(teacherId, request.getSkillIds());

        // 根据响应示例，我们只返回第一个添加成功的对象
        // 如果需要返回所有添加的对象，可以将 ApiResponse 的泛型改为 List<Skill>
        return Result.success(insertedAreas);
    }



    /*
    7.3删除某个老师的研究方向
     */

    @DeleteMapping("research-areas/{areaId}")
    public Result deleteTeacherResearchArea(@PathVariable("areaId") Long areaId,
                                            @RequestAttribute("teacherId") Integer teacherId ){
        System.out.println("删除教师研究方向,研究方向Id: "+ areaId);
        System.out.println("获取教师研究方向,教师Id: "+ teacherId);

        teacherResearchAreasResponseService.deleteResearchArea(teacherId,areaId);

        return Result.success();
    }

    /*
    7.4 获取可选研究方向列表+标记该技能老师是否已选
     */
    @GetMapping("/research-directions")
    public Result getResearchDirections(@RequestAttribute("teacherId") Integer teacherId) {

        List<ResearchAreas> directions = teacherResearchAreasResponseService.listWithSelection(teacherId);

        Map<String, Object> response = new HashMap<>();
        response.put("directions", directions);

        return Result.success(response);
    }




    /*
        8.3申请自定义研究方向
     */
    @PostMapping("/custom-research-direction")
    public Result addNewResearchArea(@RequestBody CustomResearchDirection customResearchDirection){
        System.out.println("添加研究方向: " + customResearchDirection);
        // 如果前端没有传递 status，设置为默认值 'pending'
        if (customResearchDirection.getStatus() == null) {
            customResearchDirection.setStatus("pending");
        }
        //teacherSkillService.addResearchArea(customResearchDirection);
        // 插入操作并返回插入后的数据（包含id）
        CustomResearchDirection insertedDirection = teacherSkillService.addResearchArea(customResearchDirection);
        return Result.success(insertedDirection);
    }






}
