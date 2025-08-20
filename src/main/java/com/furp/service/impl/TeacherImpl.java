package com.furp.service.impl;

import com.furp.DTO.ReviewInfoVo;
import com.furp.entity.Supervisor;
import com.furp.entity.Teacher;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.SupervisorMapper;
import com.furp.mapper.TeacherMapper;

import com.furp.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherImpl implements TeacherService {

@Autowired
private TeacherMapper teacherMapper;
@Autowired
private SupervisorMapper supervisorMapper;
@Autowired
private AnnualReviewMapper annualReviewMapper;

    public List<Teacher> findAllTeacher(){
        return teacherMapper.selectAll();
    }

    public List<Teacher> findEligibleAssessors(Integer phdId){

        List<Teacher> allTeachers = teacherMapper.selectAll();

        List<Supervisor> supervisors = supervisorMapper.findSupervisorsByPhdId(phdId);

        Set<Integer> supervisorIDs = supervisors.stream()
                .map(teacher -> teacher.getId())
                .collect(Collectors.toSet());

        if(supervisorIDs.isEmpty()){
            return allTeachers;
        }

        List<Teacher> eligibleAssessors = allTeachers.stream()
                .filter(teacher -> !supervisorIDs.contains(teacher.getId()))
                .collect(Collectors.toList());

        return eligibleAssessors;



    }

    @Override
    public List<ReviewInfoVo> findReviewScheduleByTeacherId(Integer teacherId) {
        return annualReviewMapper.findScheduledReviewByTeacherId(teacherId);
    }
}
