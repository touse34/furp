package com.furp.service;

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
}
