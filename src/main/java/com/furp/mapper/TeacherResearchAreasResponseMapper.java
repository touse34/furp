package com.furp.mapper;

import com.furp.DTO.ResearchAreaDetail;
import com.furp.DTO.TeacherResearchAreasResponseDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    /*
    * 添加教师所选研究领域信息
     */
//

    @Insert("""
        INSERT INTO teacher_skill (teacher_id, skill_id, status)
        SELECT #{teacherId}, s.id, s.status
        FROM skill s
        WHERE s.skill_name = #{skillName}
    """)
    int addResearchArea(@Param("teacherId") Integer teacherId,
                        @Param("skillName") String skillName);

    // 查 skill，拿到 skillId / status 等，用于回填到响应
    @Select("""
        SELECT s.id,
               s.skill_name AS name,
               s.status,
               s.submittedAt AS createdAt,
               s.approvedAt
        FROM skill s
        WHERE s.skill_name = #{skillName}
        LIMIT 1
    """)
    ResearchAreaDetail findSkillByName(@Param("skillName") String skillName);
//    @Insert("""
//        INSERT INTO teacher_skill (teacher_id, skill_id, status, createdAt)
//        SELECT #{teacherId}, s.id, s.status, NOW()
//        FROM skill s
//        WHERE s.skill_name = #{researchAreaDetail.name}
//    """)
//    void addResearchArea(@Param("teacherId") Integer teacherId,
//                    @Param("researchAreaDetail") ResearchAreaDetail researchAreaDetail);


}
