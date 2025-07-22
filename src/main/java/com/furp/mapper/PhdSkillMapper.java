package com.furp.mapper;

import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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


}
