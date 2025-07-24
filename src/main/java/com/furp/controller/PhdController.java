package com.furp.controller;

import com.furp.DTO.CurrentReviewVo;
import com.furp.DTO.PhdInfo;
import com.furp.DTO.SkillUpdateRequest;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.entity.Result;
import com.furp.entity.User;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.PhdMapper;
import com.furp.service.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
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
     * 根据ID查询phd
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
    /*
    修改技能
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
     * 获取当前年审状态
     */
    @GetMapping("/phd/review/current")
    public Result<CurrentReviewVo> findCurrentReview(@RequestAttribute("currentUserId") Integer userId){
        Integer phdId = phdMapper.selectPhdByUserId(userId).getId();
        CurrentReviewVo vo = annualReviewService.getCurrentReviewDetails(phdId);
        return Result.success(vo);


    }




}
