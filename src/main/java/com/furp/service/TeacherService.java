package com.furp.service;

import com.furp.DTO.ReviewInfoVo;
import com.furp.DTO.StatusUpdateDTO;
import com.furp.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    public List<Teacher> findAllTeacher();

    public List<Teacher> findEligibleAssessors(Integer phdId);

    public List<ReviewInfoVo> findReviewScheduleByTeacherId(Integer teacherId);

    public void updateTaskStatus(Integer taskId, Integer currentTeacherId, StatusUpdateDTO dto);

    public String extractAndImportTeachers(MultipartFile file);
}
