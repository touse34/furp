package com.furp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notices {

    private Integer id;

    private String title;

    private LocalDateTime createTime;

    private Integer creatorId;

    private String type;
}
