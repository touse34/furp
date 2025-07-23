package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("review_assessor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAssessor {
    private Integer id;
    private Integer annualReviewId;
    private Integer teacherId;
}

