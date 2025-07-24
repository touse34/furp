package com.furp.service.impl;

import com.furp.DTO.CurrentReviewVo;
import com.furp.DTO.PendingReviewDto;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.service.AnnualReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnualReviewImpl implements AnnualReviewService {
    @Autowired
    private AnnualReviewMapper annualReviewMapper;


    public List<PendingReviewDto> getPendingReviews() {
        return annualReviewMapper.findPendingReviews();
    }

    // 新增的 getReviewInfoByPhdId 方法
    public PendingReviewDto getReviewInfoByPhdId(Integer phdId) {
        // 调用 annualReviewMapper 获取指定 phdId 的评审信息
        return annualReviewMapper.getReviewInfoByPhdId(phdId);
    }

    public CurrentReviewVo findCurrentReviewById(Integer phdId){
        return annualReviewMapper.findCurrentReviewById(phdId);
    }

    public CurrentReviewVo getCurrentReviewDetails(Integer phdId) {

        // 1. 先获取评审的基本信息
        CurrentReviewVo reviewDetails = annualReviewMapper.findCurrentReviewById(phdId);

        // 如果找不到任何已安排的评审，直接返回 null
        if (reviewDetails == null) {
            return null;
        }

        // 2. 根据 phdId 找到对应的 reviewId (需要一个辅助查询)
        Integer reviewId = annualReviewMapper.findReviewIdByPhdId(phdId);
        if (reviewId == null) {
            return reviewDetails; // 即使有基本信息，但没ID也查不了评审员
        }

        // 3. 使用 reviewId 去获取评审员列表
        List<String> assessors = annualReviewMapper.findAssessorsByReviewId(reviewId);

        // 4. 将评审员列表设置到我们的结果对象中
        reviewDetails.setAssessors(assessors);

        // 5. 返回组装完毕的完整对象
        return reviewDetails;
    }
}
