package com.furp.mapper;

import com.furp.DTO.ResearchAreaDetail;
import com.furp.DTO.ResearchAreas;
import com.furp.DTO.TeacherResearchAreasResponseDTO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
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
            "    s.id,\n" +
            "    ts.teacher_id,\n" +
            "    s.skill_name AS name,\n" +
            "    s.status,\n" +
            "    ts.createdAt,\n" +
            "    s.approvedAt\n" +
            "FROM teacher_skill ts\n" +
            "JOIN skill s ON ts.skill_id = s.id\n" +
            "WHERE ts.teacher_id = #{teacherId};")
    List<TeacherResearchAreasResponseDTO> getByIdResearchAreas(Integer teacherId);
    /*
    * 添加教师所选研究领域信息
     */
//

    @Insert("""
        INSERT INTO teacher_skill (teacher_id, skill_id)
        SELECT #{teacherId}, s.id
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
               ts.createdAt AS createdAt,
               s.approvedAt
        FROM skill s
        JOIN furp.teacher_skill ts on s.id = ts.skill_id
        WHERE s.skill_name = #{skillName}
        LIMIT 1
    """)
    ResearchAreaDetail findSkillByName(@Param("skillName") String skillName);

    /*
    * 删除教师所选研究领域信息
     */
    @Delete("DELETE FROM teacher_skill\n" +
            "WHERE teacher_id = #{teacherId}\n" +
            "  AND skill_id = #{areaId};")
    void deleteResearchArea(Integer teacherId, Long areaId);

    /**
     * 获取教师所选研究领域信息
     *
     * @param teacherId 教师ID
     * @return 教师所选研究领域信息
     */
    @Select("SELECT\n" +
            "        s.id,\n" +
            "        s.skill_name AS name,\n" +
            "        CASE WHEN ts.teacher_id IS NULL THEN FALSE ELSE TRUE END AS selected\n" +
            "    FROM skill s\n" +
            "    LEFT JOIN teacher_skill ts\n" +
            "      ON ts.skill_id = s.id\n" +
            "     AND ts.teacher_id = #{teacherId}\n" +
            "    WHERE s.status = 'approved'\n" +
            "    ORDER BY s.skill_name ASC")
    List<ResearchAreas> listWithSelection(Integer teacherId);
    /**
     * 根据 ID 列表批量查询技能详情
     */
    @Select("<script>\n" +
            "        SELECT\n" +
            "            id,\n" +
            "            skill_name AS name,\n" +
            "            status,\n" +
            "            submittedAt AS createdAt,\n" +
            "            approvedAt\n" +
            "        FROM\n" +
            "            skill\n" +
            "        WHERE\n" +
            "            id IN\n" +
            "            <foreach collection=\"skillIds\" item=\"skillId\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{skillId}\n" +
            "            </foreach>\n" +
            "        </script>")
    List<ResearchAreaDetail> findSkillsByIds(@Param("skillIds") List<Long> skillIds);
    /**
     * 查询教师与技能的关联记录数量
     */
    @Select("SELECT COUNT(*) FROM teacher_skill WHERE teacher_id = #{teacherId} AND skill_id = #{skillId}")
    Integer findLinkCount(@Param("teacherId") Integer teacherId,@Param("skillId") Long skillId);

    /**
     * 插入一条新的教师-技能关联记录
     */
    @Insert("INSERT INTO teacher_skill (teacher_id, skill_id, createdAt) VALUES (#{teacherId}, #{skillId}, #{createdAt})")
    void insertLink(@Param("teacherId") Integer teacherId,@Param("skillId") Long skillId,@Param("createdAt") LocalDateTime now);
}
