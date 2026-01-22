package com.furp.service;

import com.furp.DTO.CustomResearchDirection;
import com.furp.DTO.ResearchAreaDetail;
import com.furp.VO.SkillSelectionVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TeacherSkillService {
    public Set<Integer> findTeacherSkillsById(Integer teacherId);
    public Map<Integer, Set<Integer>> findAllTeacherSkillsAsMap();

    /*
    新增部门
     */
    CustomResearchDirection addResearchArea(CustomResearchDirection customResearchDirection);

    List<SkillSelectionVO> getSkillSelectionForTeacher(Integer teacherId);
}
