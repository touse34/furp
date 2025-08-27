package com.furp.service;


import com.furp.DTO.ResearchAreaDetail;
import com.furp.DTO.TeacherResearchAreasResponseDTO;

public interface TeacherResearchAreasResponseService {
    /**
     * 获取教师所选研究领域列表
     *
     * @param teacherId 教师ID
     * @return 教师所选研究领域列表
     */
    TeacherResearchAreasResponseDTO getTeacherResearchAreas(Integer teacherId);

    /**
     * 添加某个教师研究领域，一次只添加一个
     *
     * @param teacherId
     * @param researchAreaDetail 教师研究领域信息
     * @return 添加后的教师研究领域信息
     */
    ResearchAreaDetail addResearchArea(Integer teacherId, ResearchAreaDetail researchAreaDetail);

    /*
    * 删除某个教师研究领域
     */
    void deleteResearchArea(Integer teacherId, Long areaId);
}
