package com.furp.service.impl;

import com.furp.mapper.TeacherSkillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherSkillImpl {
    @Autowired
    TeacherSkillMapper teacherSkillMapper;

    public Set<Integer> findTeacherSkillsById(Integer teacherId){
        return teacherSkillMapper.selectTeacherSkill(teacherId)
                .stream()
                .map(teacherSkill -> teacherSkill.getSkillId())
                .collect(Collectors.toSet());
    }
}
