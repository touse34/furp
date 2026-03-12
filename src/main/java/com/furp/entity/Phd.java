package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@TableName("Phd")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phd {
    @TableId
    private Integer id;

    private Integer userId;
    private LocalDate enrollmentDate;
    private String studentId;
    private String name;

    @TableField("is_participating_review")
    private Boolean isParticipatingReview;
}
