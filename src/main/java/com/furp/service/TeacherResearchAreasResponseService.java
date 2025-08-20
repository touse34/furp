package com.furp.service;

import com.furp.DTO.TeacherResearchAreasResponseDTO;

public interface TeacherResearchAreasResponseService {
    /**
     * 获取教师所选研究领域列表
     *
     * @param teacherId 教师ID
     * @return 教师所选研究领域列表
     */
    TeacherResearchAreasResponseDTO getTeacherResearchAreas(Integer teacherId);
}
