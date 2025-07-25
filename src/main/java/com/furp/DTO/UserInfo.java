package com.furp.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Integer id;             // 比如 "PhD001"，你可以根据需要做格式转换
    private String name;           // 姓名
    private Integer roleId;
    //private String studentId;      // 学号（如果是博士生）
    private String email;          // 邮箱
    //private String enrollmentDate; // 入学日期
    //private String supervisor;     // 导师姓名或称谓
    //private List<String> researchAreas; // 研究方向标签
    //private String status;         // 状态，如 "active"
}
