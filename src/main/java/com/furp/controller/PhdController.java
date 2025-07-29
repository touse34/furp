package com.furp.controller;

import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.PhdInfo;
import com.furp.DTO.SkillUpdateRequest;
import com.furp.VO.NoticesVO;
import com.furp.VO.SkillSelectionVO;
import com.furp.entity.PhdSkill;
import com.furp.entity.Result;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.PhdMapper;
import com.furp.response.PageResult;
import com.furp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phd")
public class PhdController {
    @Autowired
    private PhdInfoService phdinfoService;
    @Autowired
    private PhdInfoService phdInfoService;
    @Autowired
    private PhdSkillService phdSkillService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnnualReviewMapper annualReviewMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private AnnualReviewService annualReviewService;
    @Autowired
    private NoticesService noticesService;

//    @GetMapping("/phd/student/info")
//    public Result list(){
//        System.out.println("Student info");
//        List<PhdInfo> phdInfoList =  phdinfoService.findAll();
//
//        return Result.success(phdInfoList);
//
//    }

    /**
     * 根据ID查询phd 3.1.1
     * @param
     * @return
     */
    @GetMapping("/student/info")
    public Result<PhdInfo> getInfo(@RequestAttribute("currentUserId") Integer userId){


        System.out.println("当前登录者 userId = " + userId);

        // 直接通过 userId 查 phd 表
        PhdInfo phdInfo = phdInfoService.getByUserId(userId);

        if (phdInfo == null) {
            return Result.success(null);  // 或 Result.error("未绑定PhD信息")
        }

        return Result.success(phdInfo);


    }
    /**
    修改技能 3.1.2
     */
    @PutMapping("/student/research-areas")
    public Result<PhdSkill> updatePhdSkill(@RequestAttribute("currentUserId") Integer userId,
                                           @RequestBody SkillUpdateRequest request) { // <-- 修改点在这里

        // 从请求对象中获取 skillId
        Integer skillId = request.getSkillId();

        System.out.println("修改学生专业，学生ID: " + userId + ", 技能ID: " + skillId);

        PhdSkill phdSkill = phdSkillService.updatestudentSkill(userId, skillId);
        String skillName=phdSkillService.getSkillNameById(skillId);
        phdSkill.setSkillName(skillName);
        return Result.success(phdSkill);
    }

    /**
     * 获取当前年审状态 3.2.1
     */
    @GetMapping("/review/current")
    public Result<ReviewInfoVo> findCurrentReview(@RequestAttribute("phdId") Integer phdId){
        ReviewInfoVo vo = annualReviewService.getCurrentReviewDetails(phdId);
        return Result.success(vo);

    }

    /**
     * 3.2.2 获取年审历史记录
     */
    @GetMapping("/review/history")
    public Result<PageResult<ReviewInfoVo>> findHistoryReview(@RequestAttribute("phdId") Integer phdId,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "0") int size) {

        PageResult<ReviewInfoVo> vo = annualReviewService.getHistoryReviewDetails(phdId, page, size);
        return Result.success(vo);
    }

    /**
     * 3.4.1 查询该学生技能
     */
    @GetMapping("/research-areas")
    public Result<List<SkillSelectionVO>> getStudentSkillOptions(@RequestAttribute("phdId") Integer phdId){
        List<SkillSelectionVO> skillOptions = phdSkillService.getSkillSelectionForPhd(phdId);
        return Result.success(skillOptions);
    }

    /**
     * 3.3.1 获取通知列表
     */
    @GetMapping("/notices")
    public Result<PageResult<NoticesVO>> getNoticeList (@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "0") int size,
                                                        @RequestAttribute("phdId") Integer phdId){
        PageResult<NoticesVO> notices = noticesService.getNoticeList(page, size, phdId);
        return Result.success(notices);
    }


    /**
     * 3.3.2
     * 标记指定通知为已读
     * 对应接口: PUT /phd/notices/{noticeId}/read
     */
    @PutMapping("/notices/{noticeId}/read")
    public Result markAsRead(
            @PathVariable Integer noticeId,
            @RequestAttribute("phdId") Integer phdId) { // 从Token解析出的phdId

        boolean success = noticesService.markNoticeAsRead(noticeId, phdId);

        if (success) {
            return Result.success("标记成功"); // 使用一个带消息的 success 方法
        } else {
            return Result.error("操作失败，请稍后再试");
        }
    }








}
