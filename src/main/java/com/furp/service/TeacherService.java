package com.furp.service;

import com.furp.DTO.ReviewInfoVo;
import com.furp.entity.Teacher;

import java.util.List;

public interface TeacherService {
    public List<Teacher> findAllTeacher();

    public List<Teacher> findEligibleAssessors(Integer phdId);

    public List<ReviewInfoVo> findReviewScheduleByTeacherId(Integer teacherId);
}
