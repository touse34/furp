package com.furp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicTermVO {
    private String value;
    private boolean isCurrent; // 是否为当前学期
}
