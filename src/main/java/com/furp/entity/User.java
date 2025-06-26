package com.furp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    /**
     * 主键
     * @TableId: 标识这是主键字段
     * type = IdType.AUTO: 指定主键策略为数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long roleId;

    private String wechatId;


}
