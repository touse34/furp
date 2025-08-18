package com.furp.service;

import com.furp.DTO.TeacherProfileDTO;

public interface TeacherProfileService {

    /**
     * 根据教师id获取教师信息
     * @param teacherId 教师id
     * @return 教师信息
     */
    TeacherProfileDTO getById(Integer teacherId);


}
