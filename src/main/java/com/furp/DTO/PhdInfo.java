package com.furp.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhdInfo {
    private Integer studentId;
    private String name;
    private LocalDateTime enrollmentDate;
    private String skillName;

}
