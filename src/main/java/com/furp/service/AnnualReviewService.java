package com.furp.service;

import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.PendingReviewDto;
import com.furp.response.PageResult;

import java.util.List;

public interface AnnualReviewService {
    public List<PendingReviewDto> getPendingReviews();
    public PendingReviewDto getReviewInfoByPhdId(Integer phdId);
    public ReviewInfoVo findCurrentReviewById(Integer phdId);
    public ReviewInfoVo getCurrentReviewDetails(Integer phdId);
    public PageResult<ReviewInfoVo> getHistoryReviewDetails(Integer phdId, int pageNum, int pageSize);
}
