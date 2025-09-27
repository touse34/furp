package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.Schedules;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SchedulesMapper extends BaseMapper<Schedules> {
    @Select("SELECT * FROM schedules where start_time >= NOW()")
    List<Schedules> findAllFutureSchedules();

    void deleteByAnnualReviewIds(List<Integer> reviewIds);

    @Delete("DELETE FROM schedules WHERE teacher_id = #{teacherId}")
    void deleteByTeacherId(Integer teacherId);

}
