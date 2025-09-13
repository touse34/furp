package com.furp.entity;

/*import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    *//**
     * 主键
     * @TableId: 标识这是主键字段
     * type = IdType.AUTO: 指定主键策略为数据库自增
     *//*
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long roleId;

    private String wechatId;


}*/


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")  // 指定表名为 user
public class User {
    @TableId
    private Integer id;         // 用户的唯一ID

    private Integer roleId;     // 用户的角色ID
    private String wechatId;    // 用户的微信ID（可能为null）
    private String name;        // 用户的姓名
    private String password;    // 用户的密码（假设数据库中有此字段）
    private Integer phdId;
    private String email;
    private String status;
    private LocalDateTime createTime;
}
