package com.furp;



import com.furp.mapper.UserMapper;
import com.furp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest // 启动 Spring Boot 的测试环境
class YourApplicationTests {

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper

    @Test
    void testInsert() {
        User user = new User();
        user.setName("Jack");


        //调用 BaseMapper 自带的 insert 方法
        int result = userMapper.insert(user);
        System.out.println("插入影响的行数: " + result);
        System.out.println("插入后的用户ID (自增): " + user.getId());
    }

    @Test
    void testSelect() {
        // 查询所有用户
        // 传入 null 作为查询条件，表示查询所有
        List<User> userList = userMapper.selectList(null);
        System.out.println("查询到的所有用户:");
        userList.forEach(System.out::println);

        // 根据 ID 查询单个用户
        User user = userMapper.selectById(1L);
        System.out.println("ID为1的用户信息: " + user);
    }
}