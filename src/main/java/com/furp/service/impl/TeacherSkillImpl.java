package com.furp.service.impl;

import com.furp.DTO.ResearchAreaDetail;
import com.furp.mapper.TeacherSkillMapper;
import com.furp.service.TeacherSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherSkillImpl implements TeacherSkillService {
    @Autowired
    TeacherSkillMapper teacherSkillMapper;

    public Set<Integer> findTeacherSkillsById(Integer teacherId){
        return teacherSkillMapper.selectTeacherSkill(teacherId)
                .stream()
                .map(teacherSkill -> teacherSkill.getSkillId())
                .collect(Collectors.toSet());
    }

    @Override
    public void addResearchArea(ResearchAreaDetail researchAreaDetail) {
        //1.补全CreatedAt
        researchAreaDetail.setCreatedAt(LocalDateTime.now());
        //保存
        teacherSkillMapper.insert(researchAreaDetail);

    }

    public Map<Integer, Set<Integer>> findAllTeacherSkillsAsMap(){
        List<Integer> distinctTeacherId = teacherSkillMapper.selectDistinctId();
        Map<Integer, Set<Integer>> theMap = new HashMap<>();
        for(Integer teacherId : distinctTeacherId){
            Set<Integer> teacherSkills = findTeacherSkillsById(teacherId);
            theMap.put(teacherId, teacherSkills);
        }
        return theMap;
    }
}
