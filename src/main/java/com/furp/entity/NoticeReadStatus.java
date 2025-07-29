package com.furp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeReadStatus {

    private Integer id;

    private Integer noticeId;

    private Integer phdId;

    private LocalDateTime readTime;
}
