package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.entity.Teacher; // 假设 Teacher 实体已修正，不含name
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    // 使用注解方式查询所有教师
    @Select("SELECT id, user_id, is_assessor FROM teachers")
    List<Teacher> selectAll();
}
    // 更高效的方式：直接JOIN查询，返回包含教师姓名的结果 (推荐)
    // 这需要一个DTO来接收结果，这里为了简单先返回Teacher实体
//    @Select("SELECT t.id, t.user_id, t.is_accessor, u.name " +
//            "FROM teachers t JOIN users u ON t.user_id = u.id")
//    List<Teacher> selectAllWithDetails(); // Teacher 实体需要临时有 name 字段来接收
//}