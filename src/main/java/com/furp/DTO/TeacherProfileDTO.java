package com.furp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileDTO {
    private String name;
    private Integer userId;


    @JsonIgnore
    private LocalDateTime createdAt;          // 和 SQL 别名完全一致
    @JsonIgnore
    private Integer researchAreaId;           // ✔️ 一定要和 SQL 中别名一致
    @JsonIgnore
    private String researchAreaName;
    @JsonIgnore
    private String status;                    // 如果你 SQL 查了 ts.status 的话
    @JsonIgnore
    private LocalDateTime lastLoginAt;
    @JsonIgnore
    private LocalDateTime updateTime;

    private List<ResearchArea> researchAreas = new ArrayList<>();

    public void addResearchArea(Integer id, String name, String status, LocalDateTime createdAt) {
        researchAreas.add(new ResearchArea(id, name, status, createdAt));
    }

    @Data
    @AllArgsConstructor
    public static class ResearchArea {
        private Integer id;
        private String name;
        private String status;
        private LocalDateTime createdAt;
    }
}
