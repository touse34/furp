package com.furp.entity;

/*import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher {
    private int id;
    private int userId;
    private String name;
    private int isAccessor;
}*/

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("teacher")  // 指定表名为 teacher
public class Teacher {
    @TableId
    private Integer id;         // 教师的唯一ID

    private Integer userId;     // 教师的用户ID
    private String name;        // 教师的名字
    private Integer isAssessor; // 是否是评审员，1表示是，0表示不是
}
