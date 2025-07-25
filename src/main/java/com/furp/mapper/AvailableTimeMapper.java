package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.AvailableTime;
import com.furp.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AvailableTimeMapper extends BaseMapper<AvailableTime> {


    @Select("SELECT * FROM available_time WHERE teacher_id = #{teacherId}")
    List<AvailableTime> findByTeacherId(Integer teacherId);

    @Select("SELECT distinct teacher_id FROM available_time")
    List<Integer> findTeacherId();
}
