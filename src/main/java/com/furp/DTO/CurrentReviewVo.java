package com.furp.DTO;

import com.furp.entity.ReviewAssessor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentReviewVo {
    private String status;

    private Integer reviewYear;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private ReviewAssessor assessors;

}
