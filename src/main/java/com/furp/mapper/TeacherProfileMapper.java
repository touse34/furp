package com.furp.mapper;

import com.furp.DTO.TeacherProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeacherProfileMapper {

    /**
     * 根据教师ID获取教师信息
     *
     * @param userId 教师ID
     * @return 教师信息
     */
    @Select("select t.user_id as 'userId',t.name,t.create_time as 'createdAt'," +
            "s.skill_name as researchAreaName,s.id as researchAreaId, ts.status " +
            "from teacher t " +
            "LEFT JOIN teacher_skill ts ON t.id = ts.teacher_id " +
            "LEFT JOIN skill s ON ts.skill_id = s.id " +
            "where t.user_id = #{userId}")
    List<TeacherProfileDTO> getByIdWithResearchAreas(Integer userId);


    /**
     * 更新教师信息
     *
     * @param teacherProfileDTO 教师信息
     */
    @Update("update teacher set name = #{name}, update_time = #{updateTime} where user_id = #{userId}")
    void update(TeacherProfileDTO teacherProfileDTO);
}
