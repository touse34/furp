package com.furp.mapper;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PhdInfoMapper {
    /*
    Search all student info
     */
    @Select("SELECT  p.id,p.student_id AS studentId,p.name,DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate,s.skill_name AS skillName FROM phd AS p LEFT JOIN phd_skill   AS ps ON ps.phd_id  = p.id LEFT JOIN skill AS s ON s.id = ps.skill_id ORDER BY p.student_id;")
    List<PhdInfo> findAll();




    /*
    根据Id查询Phdinfo
     */
    @Select("SELECT  p.student_id                         AS studentId,\n" +
            "        p.id,\n"+
            "        p.name,\n" +
            "        DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate,\n" +
            "        s.skill_name                         AS skillName\n" +
            "FROM    phd         AS p\n" +
            "LEFT JOIN phd_skill AS ps ON ps.phd_id = p.id\n" +
            "LEFT JOIN skill     AS s  ON s.id      = ps.skill_id\n" +
            "WHERE   p.student_id = #{studentId}        -- 只查给定学号\n" +
            "ORDER BY p.student_id;\n")
    PhdInfo getById(Integer studentId);
}
