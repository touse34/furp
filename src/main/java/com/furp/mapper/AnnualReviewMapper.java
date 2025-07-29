package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.PendingReviewDto;
import com.furp.entity.AnnualReview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AnnualReviewMapper extends BaseMapper<AnnualReview> {
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

//    // 新增插入方法
//    @Insert("INSERT INTO annual_review (review_year, status, phd_id, student_id) " +
//            "VALUES (#{reviewYear}, #{status}, #{phdId}, #{studentId})")
//    void insertFinalAssignment(FinalAssignment finalAssignment);

    @Select("SELECT ar.status, ar.review_year, s.start_time, s.end_time, m.location " +
            "FROM annual_review ar " +
            "JOIN schedules s ON ar.id = s.annual_review_id " +
            "JOIN meeting_room m ON m.id = s.room_id " +
            "WHERE ar.phd_id = #{phdId} " +
            "AND ar.status = 'scheduled'")
    ReviewInfoVo findCurrentReviewById(Integer phdId);

    /**
     * 【查询二】：根据 annual_review_id 获取所有评审员的姓名列表
     * @param reviewId 评审任务的主键ID (annual_review.id)
     * @return 评审员姓名的字符串列表
     */
    @Select("SELECT t.name " +
            "FROM teacher t " +
            "JOIN review_assessor ra ON t.id = ra.teacher_id " +
            "WHERE ra.annual_review_id = #{reviewId}")
    List<String> findAssessorsByReviewId(@Param("reviewId") Integer reviewId);

    /**
            * 【新增】一个辅助查询，根据phdId找到对应的reviewId
     */
    @Select("SELECT id FROM annual_review WHERE phd_id = #{phdId} AND status = 'scheduled' LIMIT 1")
    Integer findReviewIdByPhdId(@Param("phdId") Integer phdId);


    List<ReviewInfoVo> findHistoryReviewById(Integer phdId);
}
