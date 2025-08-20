package com.furp.mapper;

import com.furp.DTO.TeacherResearchAreasResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherResearchAreasResponseMapper {

    /**
     * 获取教师所选研究领域信息
     *
     * @param teacherId 教师ID
     * @return 教师所选研究领域信息
     */
    @Select("SELECT \n" +
            "    ts.id,\n" +
            "    ts.teacher_id,\n" +
            "    s.skill_name AS name,\n" +
            "    ts.status,\n" +
            "    ts.createdAt,\n" +
            "    ts.approvedAt\n" +
            "FROM teacher_skill ts\n" +
            "JOIN skill s ON ts.skill_id = s.id\n" +
            "WHERE ts.teacher_id = #{teacherId};")
    List<TeacherResearchAreasResponseDTO> getByIdResearchAreas(Integer teacherId);
}
