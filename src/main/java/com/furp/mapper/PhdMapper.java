package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhdMapper extends BaseMapper<PhdMapper> {
    @Select("SELECT * from phd where user_id = #{userId}")
    Phd selectPhdByUserId(int userId);
    /*
     Search Student info
     */
    @Select("SELECT  p.student_id AS studentId,p.name,DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate,s.skill_name AS skillName FROM phd AS p LEFT JOIN phd_skill   AS ps ON ps.phd_id  = p.id LEFT JOIN skill AS s ON s.id = ps.skill_id ORDER BY p.student_id;")
    List<Phd> findAll();

}
