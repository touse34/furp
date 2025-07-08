package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.PendingReviewDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import com.furp.DTO.FinalAssignment;
import java.util.List;

public interface AnnualReviewMapper extends BaseMapper<AnnualReviewMapper> {
    @Select("SELECT ar.id AS review_id, ar.review_year, ar.status, p.id as phd_id, p.student_id, u.name AS student_name " +
            "FROM annual_review ar " +
            "JOIN phd p ON ar.phd_id = p.id " +
            "JOIN user u ON p.user_id = u.id " +
            "WHERE ar.status = 'pending'")

    List<PendingReviewDto> findPendingReviews();

    // 新增的根据 phdId 查询评审信息的方法
    @Select("SELECT ar.id AS review_id, ar.review_year, ar.status, p.id as phd_id, p.student_id, u.name " +
            "FROM annual_review ar " +
            "JOIN phd p ON ar.phd_id = p.id " +
            "JOIN user u ON p.student_id = u.id " +
            "WHERE ar.phd_id = #{phdId}")
    PendingReviewDto getReviewInfoByPhdId(Integer phdId);

    // 新增插入方法
    @Insert("INSERT INTO annual_review (review_year, status, phd_id, student_id) " +
            "VALUES (#{reviewYear}, #{status}, #{phdId}, #{studentId})")
    void insertFinalAssignment(FinalAssignment finalAssignment);
}
