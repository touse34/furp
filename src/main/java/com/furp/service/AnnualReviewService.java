package com.furp.service;

import com.furp.DTO.CurrentReviewVo;
import com.furp.DTO.PendingReviewDto;

import java.util.List;

public interface AnnualReviewService {
    public List<PendingReviewDto> getPendingReviews();
    public PendingReviewDto getReviewInfoByPhdId(Integer phdId);
    public CurrentReviewVo findCurrentReviewById(Integer phdId);
    public CurrentReviewVo getCurrentReviewDetails(Integer phdId);
}
