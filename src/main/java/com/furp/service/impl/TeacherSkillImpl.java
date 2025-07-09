package com.furp.service.impl;

import com.furp.mapper.TeacherSkillMapper;
import com.furp.service.TeacherSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
