package com.furp.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeAdminVO {
    private Integer id;
    private String title;
    private String content;
    private String recipientType;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime sendTime;
    private LocalDateTime scheduleTime;
}
