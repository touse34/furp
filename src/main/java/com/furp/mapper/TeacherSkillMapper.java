package com.furp.mapper;

import com.furp.DTO.CustomResearchDirection;
import com.furp.DTO.ResearchAreaDetail;
import com.furp.entity.Teacher;
import com.furp.entity.TeacherSkill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherSkillMapper {
    @Select("SELECT * from teacher_skill where teacher_id = #{teacherId}")
    List<TeacherSkill> selectTeacherSkill(Integer teacherId);

    @Select("SELECT distinct teacher_id from teacher_skill")
    List<Integer> selectDistinctId();
    /**
     * 添加新的研究领域
     * @param customResearchDirection
     */
    @Insert("insert into skill(skill_name,submittedAt,status) values(#{name},#{submittedAt},'pending')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CustomResearchDirection customResearchDirection);

    @Insert("INSERT INTO teacher_skill (teacher_id, skill_id) VALUES (#{teacherId}, #{skillId})")
    void insertskill(@Param("teacherId") Integer teacherId, @Param("skillId") Integer skillId);
}
