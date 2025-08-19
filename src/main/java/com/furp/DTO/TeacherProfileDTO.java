package com.furp.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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



    private ResearchArea researchArea;
    //private List<ResearchArea> researchAreas;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime lastLoginAt;

    /** ============ 子列表 ============ */
    @JsonIgnore
    private List<ResearchArea> researchAreas; // 研究方向数组

    // 用于接收查询结果的临时字段
    @JsonIgnore
    private Long researchAreaId;
    @JsonIgnore
    private String researchAreaName;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updateTime;

    // 手动设置 researchArea 对象
    public void setResearchAreaId(Long researchAreaId) {
        this.researchAreaId = researchAreaId;
        updateResearchArea();
    }

    public void setResearchAreaName(String researchAreaName) {
        this.researchAreaName = researchAreaName;
        updateResearchArea();
    }

    private void updateResearchArea() {
        if (researchAreaId != null && researchAreaName != null) {
            this.researchArea = new ResearchArea(researchAreaId, researchAreaName,status,createdAt);
        }
    }


    /** 内部类 / 也可独立成文件 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResearchArea {
        private Long id;
        private String name;
        private String status;
        private LocalDateTime createdAt;
    }


}