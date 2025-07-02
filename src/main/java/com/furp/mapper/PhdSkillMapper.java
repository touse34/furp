package com.furp.mapper;

import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PhdSkillMapper {
    @Select("select * from phd_skill where phd_id = #{phdId}")
    List<PhdSkill> selectSkill(Integer phdId);


}
