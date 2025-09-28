package com.furp.VO;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ResearchAreaAddResponseVO implements Serializable {
    private Long id;
    private LocalDateTime createTime;
}
