package com.furp.service;

import com.furp.DTO.PendingReviewDto;

import java.util.List;

public interface AnnualReviewService {
    public List<PendingReviewDto> getPendingReviews();
    PendingReviewDto getReviewInfoByPhdId(Integer phdId);
}
