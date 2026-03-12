package com.furp.DTO;

import lombok.Data;

@Data
public class NoticeCreateDTO {
    private String title;
    private String content;
    /** all / teacher / student */
    private String recipientType;
    /** immediate / scheduled / draft */
    private String sendType;
    /** ISO datetime string, required when sendType = "scheduled" */
    private String scheduleTime;
}
