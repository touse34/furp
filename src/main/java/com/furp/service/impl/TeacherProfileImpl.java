package com.furp.service.impl;

import com.furp.DTO.TeacherProfileDTO;
import com.furp.mapper.TeacherProfileMapper;
import com.furp.service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TeacherProfileImpl implements TeacherProfileService {
    @Autowired
    private TeacherProfileMapper teacherProfileMapper;


    @Override
    public void update(TeacherProfileDTO teacherProfileDTO) {
        // 设置更新时间
        teacherProfileDTO.setUpdateTime(LocalDateTime.now());

        // 参数验证
        if (teacherProfileDTO.getUserId() == null) {
            throw new IllegalArgumentException("教师userId不能为空");
        }

        System.out.println("准备更新教师信息：" + teacherProfileDTO);

        // 执行更新
        teacherProfileMapper.update(teacherProfileDTO);
    }

    @Override
    public TeacherProfileDTO getById(Integer teacherId) {
        return teacherProfileMapper.getById(teacherId);
    }
}
