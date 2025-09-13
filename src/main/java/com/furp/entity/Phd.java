package com.furp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("Phd")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phd {
    @TableId
    private Integer id;            // The unique ID for the PhD record

    private Integer userId;        // The ID of the associated user (Doctoral student)
    private LocalDate enrollmentDate; // The enrollment date of the PhD student
    private String studentId;
    private String name;           // Name of the PhD student
}
