package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("available_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTime {
    @TableId
    private Integer id;
    private String academicYear; // 学年

    private Integer teacherId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer timeSlotId; // 时间段ID
}
