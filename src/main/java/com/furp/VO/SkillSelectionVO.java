package com.furp.VO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillSelectionVO {

    private Integer skillId;    // 技能的ID

    private String skillName;  // 技能的名称，用于显示

    private boolean selected;   // 关键字段：标记该学生是否已拥有此技能
}
