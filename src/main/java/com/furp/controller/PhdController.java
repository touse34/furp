package com.furp.controller;

import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.PhdInfo;
import com.furp.DTO.SkillUpdateRequest;
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
    @GetMapping("/phd/student/info")
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
    @PutMapping("/phd/student/research-areas")
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
    @GetMapping("/phd/review/current")
    public Result<ReviewInfoVo> findCurrentReview(@RequestAttribute("phdId") Integer phdId){
        ReviewInfoVo vo = annualReviewService.getCurrentReviewDetails(phdId);
        return Result.success(vo);

    }

    /**
     * 3.2.2 获取年审历史记录
     */
    @GetMapping("/phd/review/history")
    public Result<PageResult<ReviewInfoVo>> findHistoryReview(@RequestAttribute("phdId") Integer phdId,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "0") int size) {

        PageResult<ReviewInfoVo> vo = annualReviewService.getHistoryReviewDetails(phdId, page, size);
        return Result.success(vo);
    }

    /**
     * 3.2.2 查询该学生技能
     */
    @GetMapping("/phd/research-areas")
    public Result<List<SkillSelectionVO>> getStudentSkillOptions(@RequestAttribute("phdId") Integer phdId){
        List<SkillSelectionVO> skillOptions = phdSkillService.getSkillSelectionForPhd(phdId);
        return Result.success(skillOptions);
    }





}
