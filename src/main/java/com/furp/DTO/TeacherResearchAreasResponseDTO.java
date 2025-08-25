// TeacherResearchAreasResponseDTO.java
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
public class TeacherResearchAreasResponseDTO {

    private List<ResearchAreaDetail> researchAreaDetail = new ArrayList<>();

    private Integer totalCount;

    private Integer pendingCount;

    private Integer approvedCount;

    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private LocalDateTime approvedAt;
    @JsonIgnore
    private String status;

    public void addResearchAreaDetail(Long id, String name, String status, LocalDateTime createdAt, LocalDateTime approvedAt){
        researchAreaDetail.add(new ResearchAreaDetail(id, name, status, createdAt, approvedAt));
    }

    /** 内部类：研究方向详细信息 */
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ResearchAreaDetail {
//
//        private Long id;
//
//        private String name;
//
//        private String status; // approved, pending, rejected
//
//        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
//        private LocalDateTime createdAt;
//
//        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
//        private LocalDateTime approvedAt; // 可能为null
//    }
}