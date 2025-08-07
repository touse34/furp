package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.AvailableTime;
import com.furp.entity.Teacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface AvailableTimeMapper extends BaseMapper<AvailableTime> {


    @Select("SELECT * FROM available_time WHERE teacher_id = #{teacherId}")
    List<AvailableTime> findByTeacherId(Integer teacherId);

    @Select("SELECT distinct teacher_id FROM available_time")
    List<Integer> findTeacherId();

    @Delete("DELETE FROM available_time WHERE teacher_id = #{teacherId} AND academic_year = #{academicYear}")
    void deleteByTeacherAndYear(@Param("teacherId") Integer teacherId, @Param("academicYear") String academicYear);

    public void batchInsertFromTimeSlots(Integer teacherId, List<Integer> timeSlotIds);

    @Select("SELECT time_slot_id FROM available_time WHERE teacher_id = #{teacherId} AND academic_year = #{academicYear}")
    public Set<Integer> findSelectedSlotIdsByTeacherAndYear(@Param("teacherId")Integer teacherId, @Param("academicYear") String academicYear);
}
