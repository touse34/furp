package com.furp.service;

import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.PendingReviewDto;

import java.util.List;

public interface AnnualReviewService {
    public List<PendingReviewDto> getPendingReviews();
    public PendingReviewDto getReviewInfoByPhdId(Integer phdId);
    public ReviewInfoVo findCurrentReviewById(Integer phdId);
    public ReviewInfoVo getCurrentReviewDetails(Integer phdId);
}
