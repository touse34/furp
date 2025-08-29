package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
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
@SaCheckRole("phd")
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
    public Result<PhdInfo> getInfo(){
        Integer userId = StpUtil.getLoginIdAsInt();


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
    @PutMapping("/student/research-area")
    public Result<PhdSkill> updatePhdSkill(@RequestBody List<Integer> skillIds) { // <-- 修改点在这里

        Integer userId = StpUtil.getLoginIdAsInt();

        System.out.println("修改学生专业，学生ID: " + userId + ", 技能ID: " + skillIds);

        int count = phdSkillService.updatestudentSkill(userId, skillIds);

        return Result.success("成功修改了" + count + "个技能。");
    }

    /**
     * 获取当前年审状态 3.2.1
     */
    @GetMapping("/review/current")
    public Result<ReviewInfoVo> findCurrentReview(){
        Integer phdId = StpUtil.getSession().getInt("phdId");
        ReviewInfoVo vo = annualReviewService.getCurrentReviewDetails(phdId);
        return Result.success(vo);

    }

    /**
     * 3.2.2 获取年审历史记录
     */
    @GetMapping("/review/history")
    public Result<PageResult<ReviewInfoVo>> findHistoryReview(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "0") int size) {
        Integer phdId = StpUtil.getSession().getInt("phdId");
        PageResult<ReviewInfoVo> vo = annualReviewService.getHistoryReviewDetails(phdId, page, size);
        return Result.success(vo);
    }

    /**
     * 3.4.1 查询该学生技能
     */
    @GetMapping("/research-areas")
    public Result<List<SkillSelectionVO>> getStudentSkillOptions(){
        Integer phdId = StpUtil.getSession().getInt("phdId");
        List<SkillSelectionVO> skillOptions = phdSkillService.getSkillSelectionForPhd(phdId);
        return Result.success(skillOptions);
    }

    /**
     * 3.3.1 获取通知列表
     */
    @GetMapping("/notices")
    public Result<PageResult<NoticesVO>> getNoticeList (@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "0") int size){
        Integer phdId = StpUtil.getSession().getInt("phdId");
        PageResult<NoticesVO> notices = noticesService.getNoticeList(page, size, phdId);
        return Result.success(notices);
    }


    /**
     * 3.3.2
     * 标记指定通知为已读
     * 对应接口: PUT /phd/notices/{noticeId}/read
     */
    @PutMapping("/notices/{noticeId}/read")
    public Result<Void> markAsRead( //对于一个不返回任何具体业务数据、只返回成功或失败状态的接口，最佳实践是使用 Void 作为泛型类型。
            @PathVariable Integer noticeId) {
        Integer phdId = StpUtil.getSession().getInt("phdId");
        boolean success = noticesService.markNoticeAsRead(noticeId, phdId);

        if (success) {
            return Result.success("标记成功"); // 使用一个带消息的 success 方法
        } else {
            return Result.error("操作失败，请稍后再试");
        }
    }








}
