package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("annual_review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualReview {
    private Integer id;
    private Integer phdId;
    private Integer reviewYear;
    private Integer status;

}
