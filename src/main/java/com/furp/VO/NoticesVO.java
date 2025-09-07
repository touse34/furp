package com.furp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticesVO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime time;
    private boolean read;
}
