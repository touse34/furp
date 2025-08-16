package com.furp.mapper;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PhdInfoMapper {
    /*
    Search all student info
    */
    @Select("SELECT p.id, p.student_id AS studentId, p.name, " +
            "DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate, " +
            "s.id AS researchAreaId, s.skill_name AS researchAreaName " +
            "FROM phd AS p " +
            "LEFT JOIN phd_skill ps ON ps.phd_id = p.id " +
            "LEFT JOIN skill s ON s.id = ps.skill_id " +
            "ORDER BY p.student_id")
    List<PhdInfo> findAll();

    /*
    根据Id查询Phdinfo
    */
    @Select("SELECT p.student_id AS studentId, p.id, p.name, " +
            "DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate, " +
            "s.id AS researchAreaId, s.skill_name AS researchAreaName " +
            "FROM phd AS p " +
            "LEFT JOIN phd_skill ps ON ps.phd_id = p.id " +
            "LEFT JOIN skill s ON s.id = ps.skill_id " +
            "WHERE p.student_id = #{studentId} " +
            "LIMIT 1")
    PhdInfo getById(Integer studentId);

    /*
    根据UserId查询Phdinfo
    */
    @Select("SELECT p.id, p.student_id AS studentId, p.name, " +
            "DATE_FORMAT(p.enrollment_date, '%Y-%m-%d') AS enrollmentDate, " +
            "s.id AS researchAreaId, s.skill_name AS researchAreaName " +
            "FROM phd p " +
            "LEFT JOIN phd_skill ps ON p.id = ps.phd_id " +
            "LEFT JOIN skill s ON ps.skill_id = s.id " +
            "WHERE p.user_id = #{userId} " +
            "LIMIT 1")
    PhdInfo getByUserId(@Param("userId") Integer userId);

}
