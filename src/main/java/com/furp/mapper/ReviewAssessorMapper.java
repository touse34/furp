package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.ReviewAssessor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewAssessorMapper extends BaseMapper<ReviewAssessor> {

    @Select("select count(*) from review_assessor where teacher_id = #{teacherId} and annual_review_id = #{reviewId}")
    public int isExist(Integer teacherId, Integer reviewId);

}
