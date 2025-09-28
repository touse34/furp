package com.furp.VO;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PendingResearchAreaVO implements Serializable {
    private Integer id;
    private String name;
    private String submitter; // The submitter's name
    private LocalDateTime submitDate;
    private String status;
}
