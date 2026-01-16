package com.furp.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentImportDTO {

    @ExcelProperty("Student")
    private String name;

    @ExcelProperty("Email")
    private String email;

    @ExcelProperty("Student ID")
    private String studentId;

    @ExcelProperty("Lead Supervisor")
    private String leadSupervisor;

    @ExcelProperty("2nd Supervisor")
    private String secondSupervisor;

    @ExcelProperty("3rd Supervisor")
    private String thirdSupervisor;

    @ExcelProperty("4th Supervisor")
    private String fourthSupervisor;

    @ExcelProperty("Start Date")
    private String enrollmentDate;
}
