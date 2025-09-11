package com.furp.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户列表返回数据视图对象
 */
@Data
// 这个注解非常有用：如果某个字段的值是 null，那么在生成 JSON 字符串时，就不会包含这个字段。
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO implements Serializable {

    @JsonIgnore // 这个注解表示在生成JSON时，忽略这个字段。它只用于后端逻辑。
    private Integer roleId; // 增加 roleId 用于后端判断
    // --- ADD THIS FIELD ---
    @JsonIgnore // Important: This hides the field from the final JSON output.
    private Integer phdId;
    @JsonIgnore // 【新增】
    private Integer teacherId; // 【新增】用于后端逻辑，存放 teacher.id

    // --- 通用字段 ---
    private String id;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createTime;

    // --- PhD 特有字段 ---
    private String studentId;
    private LocalDate enrollmentDate;

    // --- Teacher 特有字段 (我们可以预留一些，比如职称) ---
    // private String title;

    // --- 复杂/关联字段 ---
    private List<String> supervisors; // 导师ID列表
    private String mainSupervisor;    // 主导师ID
    private List<String> researchAreas; // 研究领域名称列表
}
