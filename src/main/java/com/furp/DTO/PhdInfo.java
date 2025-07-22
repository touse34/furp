package com.furp.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhdInfo {
    private Integer id;
    private Integer studentId;
    private String name;
    private LocalDateTime enrollmentDate;
    private String skillName;
    private Integer totalReviews;
    private String avatar;

    /** ============ 子列表 ============ */
    private List<ResearchArea> researchAreas; // 研究方向数组

    /** 内部类 / 也可独立成文件 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResearchArea {
        private Long   id;
        private String name;
    }

}
