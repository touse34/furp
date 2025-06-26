package com.furp.service.impl;

import com.furp.entity.Teacher;
import com.furp.mapper.TeacherMapper;
import com.furp.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TeacherService {
@Autowired
private TeacherMapper teacherMapper;

    public List<Teacher> findAllTeacher(){
        return teacherMapper.selectAll();
    }

    public List<Teacher> findEligibleAssessors(){

    }
}
