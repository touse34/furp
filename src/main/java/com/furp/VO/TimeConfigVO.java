package com.furp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeConfigVO {
    private Integer id;
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime;   // 结束时间

    private int selected; // 是否选中，1表示选中，0表示未选中

}
