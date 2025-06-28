package com.furp.service.impl;

import com.furp.entity.Teacher;
import com.furp.mapper.SupervisorMapper;
import com.furp.mapper.TeacherMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherService {
@Autowired
private TeacherMapper teacherMapper;
private SupervisorMapper supervisorMapper;

    public List<Teacher> findAllTeacher(){
        return teacherMapper.selectAll();
    }

    public List<Teacher> findEligibleAssessors(Integer phdId){

        List<Teacher> allTeachers = teacherMapper.selectAll();

        List<Teacher> supervisors = supervisorMapper.findSupervisorsByPhdId(phdId);

        Set<Integer> supervisorIDs = supervisors.stream()
                .map(teacher -> teacher.getId())
                .collect(Collectors.toSet());

        if(supervisorIDs.isEmpty()){
            return allTeachers;
        }


    }
}
