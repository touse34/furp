package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.Supervisor;
import com.furp.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface SupervisorMapper extends BaseMapper<Supervisor>{
    /**
     * 根据博士生ID (phd.id)，查询其所有导师的完整信息。
     *
     * @param phdId 博士生表的主键 (phd.id)
     * @return 该学生的所有导师列表
     */

    @Select("select t.* from teacher t join supervisor s ON t.id = s.teacher_id where s.phd_id = #{phdId}")
    List<Teacher> findSupervisorsByPhdId(@Param("phdId") Integer phdId) ;


}
