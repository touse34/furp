package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("Skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private Integer id;
    private String skillName;
    private String status;
    private String rejectionReason; // ADD THIS FIELD
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;

}
