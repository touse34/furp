package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Select;

@TableName("phd_skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhdSkill {
    @TableId
    //private Integer id;

    private Integer phdId;
    private Integer skillId;
    private String skillName;
}
