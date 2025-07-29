package com.furp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticesVO {
    private Integer id;
    private String title;
    private String content;
    private String time;
    private boolean read;
}
