package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.ResearchAreaPageQueryDTO;
import com.furp.VO.ResearchAreasVO;
import com.furp.entity.Skill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SkillMapper extends BaseMapper<Skill> {

    @Select("select s.id from skill s " +
            "join phd_skill p on p.skill_id = s.id " +
            "where phd_id = #{phdId}")
    List<Integer> selectSkillIdByPhdId(Integer phdId);

    @Select("SELECT id FROM skill WHERE skill_name = #{name}")
    Integer getIdByName(String name);


    Page<ResearchAreasVO> pageQuery(ResearchAreaPageQueryDTO queryDTO);
}
