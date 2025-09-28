package com.furp.VO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ResearchAreasVO implements Serializable {
    private Integer id;
    private String name;
    private Integer userCount; // This will be a calculated field
    private LocalDate createDate;
    private String status;

}
