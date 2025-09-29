package com.furp.DTO;

import lombok.Data;

@Data
public class ResearchAreaReviewDTO {
    private String action; // "approve" or "reject"
    private String reason;

}
