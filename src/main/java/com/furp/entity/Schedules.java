package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schedules")
public class Schedules {
    @TableId
    private Integer id;

    private Integer annualReviewId;
    private Integer teacherId;
    private Integer roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
