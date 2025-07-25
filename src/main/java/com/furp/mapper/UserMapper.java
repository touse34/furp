package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.DTO.UserInfo;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户表的 Mapper 接口
 * 继承 BaseMapper<User> 后，就自动拥有了对 User 表的 CRUD 能力。
 * * @Mapper 注解是可选的，因为主启动类中已经使用了 @MapperScan。
 * 但保留它也无害。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如果有复杂的、自定义的 SQL 查询，可以在这里定义方法，并在 XML 文件中实现
    // 但对于基础的 CRUD, 这里已经完全足够了，无需任何代码。

    @Select("SELECT * from user where name = #{username}")
    User getUserByName(String username);


    /*
    查询所有的userInfo
     */
    @Select("SELECT id, role_id, `name` FROM `user`")
    List<UserInfo> findAll();



    @Select("SELECT id, role_id, `name` FROM `user` where role_id= #{roleId}")
    List<UserInfo> findByRole(Integer roleId);
}
// 注意：这里已经删除了末尾多余的大括号
