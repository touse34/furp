package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherTimeSelectionDTO {
    private String academicYear; // 学期
    private List<Integer> slotIds; // 选中的时间配置ID列表
}
