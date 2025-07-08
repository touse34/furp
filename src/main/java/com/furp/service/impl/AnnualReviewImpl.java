package com.furp.service.impl;

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
}
