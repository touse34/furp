package com.furp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        // 1. 设置过滤条件
        LambdaQueryWrapper<Skill> queryWrapper = new LambdaQueryWrapper<>();
        // 对应数据库里的字段 status = 'approved'
        queryWrapper.eq(Skill::getStatus, "approved");
        // 按名称排序
        queryWrapper.orderByAsc(Skill::getSkillName);

        // 2. 🟢 关键修改：把 queryWrapper 传进去！
        List<Skill> approvedSkills = skillMapper.selectList(queryWrapper);

        // 3. 剩下的逻辑不变...
        Set<Integer> selectedSkillIds = new HashSet<>(skillMapper.selectSkillIdByTeacherId(teacherId));

        return approvedSkills.stream()
                .map(skill -> new SkillSelectionVO(
                        skill.getId(),
                        skill.getSkillName(),
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
