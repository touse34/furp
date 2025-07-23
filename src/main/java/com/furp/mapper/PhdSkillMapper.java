package com.furp.mapper;

import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PhdSkillMapper {

    /**
     * 更新 PhD 的研究领域
     *
     * @param phdSkill
     */
    @Update("update phd_skill set skill_id = #{skillId} where phd_id = #{phdId}")
    void updatestudentSkill(PhdSkill phdSkill);

    @Select("select * from phd_skill where phd_id = #{phdId}")
    List<PhdSkill> selectSkill(Integer phdId);

    // 1. 删除学生现有的所有研究方向
    @Delete("DELETE FROM phd_skill WHERE phd_id = #{phdId}")
    int deletePhdSkillsByPhdId(@Param("phdId") Integer phdId);

    // 2. 插入新的研究方向
    @Insert("INSERT INTO phd_skill (phd_id, skill_id) VALUES (#{phdId}, #{skillId})")
    int insertPhdSkill(@Param("phdId") Integer phdId, @Param("skillId") Integer skillId);

    @Select("SELECT skill.skill_name FROM skill WHERE id = #{skillId}")
    String getSkillNameById(@Param("skillId") Integer skillId);


}
