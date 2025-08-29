package com.furp.DTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ResearchAreaRequest {

    @NotEmpty(message = "研究方向ID列表不能为空")
    private List<Long> skillIds;

    // Getter
    public List<Long> getSkillIds() {
        return skillIds;
    }

    // Setter
    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }
}