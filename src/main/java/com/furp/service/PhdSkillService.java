package com.furp.service;

import com.furp.VO.SkillSelectionVO;
import com.furp.entity.PhdSkill;

import java.util.List;
import java.util.Set;

public interface PhdSkillService {
    public Set<Integer> findPhdSkillsById(Integer phdId);

    /*
     * @param phdSkill
     */
    PhdSkill updatestudentSkill(Integer userId, Integer skillId);

    String getSkillNameById(Integer skillId);

    /**
     * 获取某个学生的所有技能选项（并标记出已选项）
     * @param phdId 博士生的主键ID
     * @return 包含选中状态的技能列表
     */
    public List<SkillSelectionVO> getSkillSelectionForPhd(Integer phdId);
}
