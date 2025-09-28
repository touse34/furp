package com.furp.DTO;


import lombok.Data;

import java.io.Serializable;

@Data
public class ResearchAreaPageQueryDTO implements Serializable {
    private String keyword;
    private int page = 1;
    private int size = 20;
}