package com.furp.service.impl;

import com.furp.DTO.CustomResearchDirection;
import com.furp.DTO.ResearchAreaDetail;
import com.furp.VO.SkillSelectionVO;
import com.furp.entity.Skill;
import com.furp.mapper.SkillMapper;
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
    @Autowired
    private SkillMapper skillMapper;

    public Set<Integer> findTeacherSkillsById(Integer teacherId){
        return teacherSkillMapper.selectTeacherSkill(teacherId)
                .stream()
                .map(teacherSkill -> teacherSkill.getSkillId())
                .collect(Collectors.toSet());
    }

    @Override
    public CustomResearchDirection addResearchArea(CustomResearchDirection customResearchDirection) {
        //1.补全CreatedAt
        customResearchDirection.setSubmittedAt(LocalDateTime.now());
        //保存
        teacherSkillMapper.insert(customResearchDirection);

        return customResearchDirection;

    }

    @Override
    public List<SkillSelectionVO> getSkillSelectionForTeacher(Integer teacherId) {
        List<Skill> allSkills = skillMapper.selectList(null);
        Set<Integer> selectedSkillIds = new HashSet<>(skillMapper.selectSkillIdByTeacherId(teacherId));

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
