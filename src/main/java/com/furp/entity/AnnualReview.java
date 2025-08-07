package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("annual_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualReview {
    @TableId
    private Integer id;

    private Integer phdId;
    private String reviewYear;
    private String status;

}
