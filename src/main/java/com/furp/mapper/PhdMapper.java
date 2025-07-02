package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.PhdSkill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhdMapper extends BaseMapper<PhdMapper> {
    @Select("select * from phd_skill where phd_id = #{phdId}")
    List<PhdSkill> selectSkill(Integer phdId);

}
