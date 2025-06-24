package com.furp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furp.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表的 Mapper 接口
 * 继承 BaseMapper<User> 后，就自动拥有了对 User 表的 CRUD 能力。
 * * @Mapper 注解是可选的，因为主启动类中已经使用了 @MapperScan。
 * 但保留它也无害。
 */

public interface UserMapper extends BaseMapper<User> {
    // 如果有复杂的、自定义的 SQL 查询，可以在这里定义方法，并在 XML 文件中实现
    // 但对于基础的 CRUD, 这里已经完全足够了，无需任何代码。
}
// 注意：这里已经删除了末尾多余的大括号
