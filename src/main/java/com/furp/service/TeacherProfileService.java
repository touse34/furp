package com.furp.service;

import com.furp.DTO.TeacherProfileDTO;

public interface TeacherProfileService {

    /**
     * 根据教师id获取教师信息
     * @param teacherId 教师id
     * @return 教师信息
     */
    TeacherProfileDTO getById(Integer teacherId);

    /**
     * 更新教师信息
     * @param teacherProfileDTO 教师信息（包含id用作更新条件）
     */
    void update(TeacherProfileDTO teacherProfileDTO);
}
