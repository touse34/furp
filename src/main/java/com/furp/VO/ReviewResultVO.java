package com.furp.VO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReviewResultVO implements Serializable {
    private String status;
    private LocalDateTime processTime;
}
