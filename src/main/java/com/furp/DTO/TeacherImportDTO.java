package com.furp.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TeacherImportDTO {

    // 对应你截图里的列名，必须一模一样
    @ExcelProperty("Assessor 1")
    private String assessor1;

    @ExcelProperty("Assessor 2")
    private String assessor2;

    @ExcelProperty("Lead Supervisor")
    private String leadSupervisor;

    @ExcelProperty("2nd Supervisor")
    private String secondSupervisor;

    @ExcelProperty("3rd Supervisor")
    private String thirdSupervisor;

    @ExcelProperty("4th Supervisor")
    private String fourthSupervisor;
}
