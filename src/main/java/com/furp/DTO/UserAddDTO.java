package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddDTO  implements Serializable {
    // --- 通用字段 (博士生和教师都有) ---
    private String type;
    private String name;
    private String id; // 这个字段将接收 "PhD2021003" 或 "T2021003"
    private String email;
    private List<String> researchAreas;

    // --- 博士生特有字段 (添加教师时，这些字段会是 null) ---
    private LocalDate enrollmentDate;
    private List<String> supervisors;
    private String mainSupervisor;

    // --- 教师特有字段 (未来可以扩展) ---
    // private String title; // 例如：职称


}
