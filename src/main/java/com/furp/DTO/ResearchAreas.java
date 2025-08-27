package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResearchAreas {
    private Long id;
    private String name;
    private Boolean selected;
}
