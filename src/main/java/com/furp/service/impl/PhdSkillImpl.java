package com.furp.service.impl;

import com.furp.entity.PhdSkill;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.PhdSkillMapper;
import com.furp.service.PhdService;
import com.furp.service.PhdSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void updatestudentSkill(PhdSkill phdSkill) {
        phdSkillMapper.updatestudentSkill(phdSkill);
    }

    public Set<Integer> findPhdSkillsById(Integer phdId){
        return phdSkillMapper.selectSkill(phdId).stream().map(phdSkill -> phdSkill.getSkillId()).collect(Collectors.toSet());
    }


}
