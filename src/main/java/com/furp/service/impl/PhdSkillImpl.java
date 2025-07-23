package com.furp.service.impl;

import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.PhdSkillMapper;
import com.furp.service.PhdService;
import com.furp.service.PhdSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Set<Integer> findPhdSkillsById(Integer phdId){
        return phdSkillMapper.selectSkill(phdId).stream().map(phdSkill -> phdSkill.getSkillId()).collect(Collectors.toSet());
    }


}
