package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("Skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private Integer id;
    private String skillName;

}
