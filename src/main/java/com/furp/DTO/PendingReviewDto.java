package com.furp.DTO;

import lombok.Data;

@Data
public class PendingReviewDto {
    private Integer reviewId;
    private Integer reviewYear;
    private String status;
    private Integer studentId;
    private String studentName;
    private Integer phdId; // 把phd表的主键也加上

    public void setSupervisorId(int i) {

    }
}

