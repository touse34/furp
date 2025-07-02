package com.furp.entity;

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
    private Integer id;
    private Integer teacherId;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
}
