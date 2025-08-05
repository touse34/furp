package com.furp.entity;

import lombok.Data;

@Data
public class TimeSlots {
    private Integer id;          // 时间段ID
    private String startTime;    // 开始时间
    private String endTime;      // 结束时间
    private String academicYear;    // 星期几（如：Monday, Tuesday等）
    private Integer isActive;   // 是否启用（1表示启用，0表示禁用）
}
