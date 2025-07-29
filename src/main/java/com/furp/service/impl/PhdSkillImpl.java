package com.furp.service.impl;

import com.furp.VO.SkillSelectionVO;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.entity.Skill;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.PhdSkillMapper;
import com.furp.mapper.SkillMapper;
import com.furp.service.PhdSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhdSkillImpl implements PhdSkillService {
    @Autowired
    private PhdSkillMapper phdSkillMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private SkillMapper skillMapper;

    @Override
    @Transactional
    public PhdSkill updatestudentSkill(Integer userId, Integer newSkillId) {
        Phd phd = phdMapper.selectPhdByUserId(userId);
        if (phd == null) {
            throw new RuntimeException("学生未找到或未关联PhD信息");
        }
        Integer phdId = phd.getId();
        // 2. 删除该学生所有旧的研究方向
        phdSkillMapper.deletePhdSkillsByPhdId(phdId);
        // 3. 插入新的研究方向
        phdSkillMapper.insertPhdSkill(phdId, newSkillId);

        PhdSkill phdSkill = new PhdSkill();
        phdSkill.setPhdId(phdId);
        phdSkill.setSkillId(newSkillId);
        return phdSkill;
    }

    @Override
    public String getSkillNameById(Integer skillId) {
        return phdSkillMapper.getSkillNameById(skillId);
    }

    @Override
    public List<SkillSelectionVO> getSkillSelectionForPhd(Integer phdId) {
        List<Skill> allSkills = skillMapper.selectList(null);
        Set<Integer> selectedSkillIds = new HashSet<>(skillMapper.selectSkillIdByPhdId(phdId));

        // 3. 在内存中进行组装
        // 遍历所有技能，为每个技能创建一个 SkillSelectionVO，并设置其 selected 状态
        return allSkills.stream()
                .map(skill -> new SkillSelectionVO(
                        skill.getId(),
                        skill.getSkillName(),
                        // 核心逻辑：如果学生的已选技能ID集合中包含当前技能的ID，则 selected 为 true
                        selectedSkillIds.contains(skill.getId())
                ))
                .collect(Collectors.toList());
    }

    public Set<Integer> findPhdSkillsById(Integer phdId){
        return phdSkillMapper.selectSkillByPhdId(phdId).stream().map(phdSkill -> phdSkill.getSkillId()).collect(Collectors.toSet());
    }


}
