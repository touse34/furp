package com.furp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileDTO {

    private String name;
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime lastLoginAt;

    private String researchAreas;
    //private List<ResearchArea> researchAreas;
//
//    /** 内部类：研究领域 */
//
//    public static class ResearchArea {
//        private Long id;
//        private String name;
//        private String status;  // approved, pending, rejected 等
//
//        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        private LocalDateTime createdAt;

}