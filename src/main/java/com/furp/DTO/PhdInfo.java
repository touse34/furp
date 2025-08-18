package com.furp.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@JsonPropertyOrder({
        "id", "name", "studentId", "totalReviews", "currentYear", "avatar", "researchArea"
})

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhdInfo {
    private Integer id;
    private String name;
    private Integer studentId;
    private Integer totalReviews;

    @JsonProperty("currentYear")
    public String getCurrentYear() {
        if (enrollmentDate == null) return null;
        int year = LocalDateTime.now().getYear() - enrollmentDate.getYear() + 1;
        return "Year " + year; // 或者根据你的业务规则拼接
    }

    @JsonIgnore
    private LocalDateTime enrollmentDate;  //由这个来计算currentyear

    private String avatar;//数据库中暂时还没有

    // 改为对象格式，单个研究方向
    private ResearchArea researchArea;

    /** ============ 子列表 ============ */
    @JsonIgnore
    private List<ResearchArea> researchAreas; // 研究方向数组

    // 用于接收查询结果的临时字段
    @JsonIgnore
    private Long researchAreaId;
    @JsonIgnore
    private String researchAreaName;

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
            this.researchArea = new ResearchArea(researchAreaId, researchAreaName);
        }
    }

    /** 内部类 / 也可独立成文件 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResearchArea {
        private Long id;
        private String name;
    }
}
