package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime;   // 结束时间

    /**
     * 判断两个时间段是否重叠
     */
    public boolean overlaps(TimeSlot other) {
        // 如果满足以下两个条件，则两个时间段有重叠：
        // 1. 当前时间段的开始时间在另一个时间段的结束时间之前
        // AND
        // 2. 另一个时间段的开始时间在当前时间段的结束时间之前
        return this.startTime.isBefore(other.endTime) &&
                other.startTime.isBefore(this.endTime);
        //在这个 overlaps 方法中，this.startTime.isBefore(other.endTime) 确保了第一个时间段没有完全在第二个时间段之后。
        //而另一个条件 other.startTime.isBefore(this.endTime) 则确保了第二个时间段没有完全在第一个时间段之后。
        //这两个条件同时满足，才表示它们有重叠。
        //这个逻辑是处理时间或区间重叠问题的标准方法之一
    }
}