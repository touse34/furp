package com.furp.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeadlineVO {
    private LocalDateTime deadline;     // 截止时间
    private LocalDateTime currentTime;  // 服务器当前时间，防止前端乱改本地时间作弊
    private Boolean isOpen;             // 告诉前端通道是否还开着
}
