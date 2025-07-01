package com.furp.service.impl;

import com.furp.DTO.response.PendingReviewDto;
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
}
