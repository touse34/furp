package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher {
    private int id;
    private int userId;
    private String name;
    private int isAccessor;
}
