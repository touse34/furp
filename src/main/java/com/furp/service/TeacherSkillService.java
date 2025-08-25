package com.furp.service;

import com.furp.DTO.ResearchAreaDetail;

import java.util.Map;
import java.util.Set;

public interface TeacherSkillService {
    public Set<Integer> findTeacherSkillsById(Integer teacherId);
    public Map<Integer, Set<Integer>> findAllTeacherSkillsAsMap();

    /*
    新增部门
     */
    void addResearchArea(ResearchAreaDetail researchAreaDetail);

}
