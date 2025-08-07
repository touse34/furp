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

    private Boolean selected; // 是否选中

}
