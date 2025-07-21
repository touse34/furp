package com.furp.mapper;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhdInfoMapper {
    /*
    Search all student info
     */
    @Select("SELECT  p.student_id AS studentId,p.name,DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate,s.skill_name AS skillName FROM phd AS p LEFT JOIN phd_skill   AS ps ON ps.phd_id  = p.id LEFT JOIN skill AS s ON s.id = ps.skill_id ORDER BY p.student_id;")
    List<PhdInfo> findAll();
}
