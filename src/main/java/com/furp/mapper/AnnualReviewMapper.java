package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.response.PendingReviewDto;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnnualReviewMapper extends BaseMapper<AnnualReviewMapper> {
    @Select("SELECT ar.id AS review_id, ar.review_year, ar.status, p.id as phd_id, p.student_id, u.name AS student_name " +
            "FROM annual_review ar " +
            "JOIN phd p ON ar.phd_id = p.id " +
            "JOIN user u ON p.user_id = u.id " +
            "WHERE ar.status = 'pending'")

    List<PendingReviewDto> findPendingReviews();
}
