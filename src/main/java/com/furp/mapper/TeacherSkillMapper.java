package com.furp.mapper;

import com.furp.entity.Teacher;
import com.furp.entity.TeacherSkill;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherSkillMapper {
    @Select("SELECT * from teacher_skill where teacher_id = #{teacherId}")
    List<TeacherSkill> selectTeacherSkill(Integer teacherId);

    @Select("SELECT distinct teacher_id from teacher_skill")
    List<Integer> selectDistinctId();
}
