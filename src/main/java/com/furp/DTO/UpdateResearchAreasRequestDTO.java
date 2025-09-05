package com.furp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResearchAreasRequestDTO {
    /**
     * 研究方向的ID列表。
     * 字段名 "researchAreaIds" 必须与前端发送的JSON中的键名完全一致。
     * {
     * "researchAreaIds": [1, 5, 8]
     * }
     */
    private List<Long> researchAreaIds;

}
