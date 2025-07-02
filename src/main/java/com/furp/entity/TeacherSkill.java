package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("teacher_skill")
public class TeacherSkill {
    private Integer id;
    private Integer teacherId;
    private Integer skillId;
}
