package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewInfoVo {
    private String status;

    private String reviewYear;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private List<String> assessors;

    private String comments;

    private String researchField;

    private String phdName;

    private String coAssessor;

}
