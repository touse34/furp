package com.furp.service;

import com.furp.DTO.response.PendingReviewDto;

import java.util.List;

public interface AnnualReviewService {
    public List<PendingReviewDto> getPendingReviews();
}
