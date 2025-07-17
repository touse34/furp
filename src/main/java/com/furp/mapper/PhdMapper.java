package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhdMapper extends BaseMapper<PhdMapper> {
    @Select("SELECT * from phd where user_id = #{userId}")
    Phd selectPhdByUserId(int userId);
}
