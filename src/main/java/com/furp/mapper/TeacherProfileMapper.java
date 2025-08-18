package com.furp.mapper;

import com.furp.DTO.TeacherProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherProfileMapper {

    /**
     * 根据教师ID获取教师信息
     *
     * @param teacherId 教师ID
     * @return 教师信息
     */
    @Select("select t.user_id as 'userId',t.name," +
            "s.skill_name as researchAreaName,s.id as researchAreaId " +
            "from teacher t " +
            "LEFT JOIN teacher_skill ts ON t.id = ts.teacher_id " +
            "LEFT JOIN skill s ON ts.skill_id = s.id " +
            "where t.id = #{teacherId}")
    TeacherProfileDTO getById(Integer teacherId);
}
