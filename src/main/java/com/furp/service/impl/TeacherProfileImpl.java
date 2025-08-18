package com.furp.service.impl;

import com.furp.DTO.TeacherProfileDTO;
import com.furp.mapper.TeacherProfileMapper;
import com.furp.service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherProfileImpl implements TeacherProfileService {
    @Autowired
    private TeacherProfileMapper teacherProfileMapper;



    @Override
    public TeacherProfileDTO getById(Integer teacherId) {
        return teacherProfileMapper.getById(teacherId);
    }
}
