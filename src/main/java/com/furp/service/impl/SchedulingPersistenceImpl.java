package com.furp.service.impl;

import com.furp.DTO.FinalAssignment;
import com.furp.DTO.TimeSlot;
import com.furp.entity.AnnualReview;
import com.furp.entity.ReviewAssessor;
import com.furp.entity.Schedules;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.ReviewAssessorMapper;
import com.furp.mapper.SchedulesMapper;
import com.furp.service.SchedulingPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchedulingPersistenceImpl implements SchedulingPersistenceService {

    @Autowired
    private AnnualReviewMapper annualReviewMapper;
    @Autowired private ReviewAssessorMapper reviewAssessorMapper;
    @Autowired private SchedulesMapper schedulesMapper;

    /**
     * 将最终排程结果持久化到数据库。
     * @Transactional 注解现在可以正常工作了，因为它被外部类调用。
     */

    @Transactional
    public void persistSchedule(List<FinalAssignment> finalAssignments) {
        if (finalAssignments == null || finalAssignments.isEmpty()) {
            return;
        }

        System.out.println("\n--- 阶段四：开始将最终排程结果写入数据库 ---");

        for (FinalAssignment assignment : finalAssignments) {
            Integer reviewId = assignment.getReviewInfo().getReviewId();

            // 1. 更新 annual_review 表
            AnnualReview reviewToUpdate = new AnnualReview();
            reviewToUpdate.setId(reviewId);
            reviewToUpdate.setStatus("scheduled");
            annualReviewMapper.updateById(reviewToUpdate);

            // 2. 插入 review_assessor 表
            reviewAssessorMapper.insert(new ReviewAssessor(null, reviewId, assignment.getTeacher1().getId()));
            reviewAssessorMapper.insert(new ReviewAssessor(null, reviewId, assignment.getTeacher2().getId()));

            // 3. 插入 schedules 表
            String title = "Annual Review for " + assignment.getReviewInfo().getStudentName();
            TimeSlot slot = assignment.getTimeSlot();
            schedulesMapper.insert(new Schedules(null, reviewId, assignment.getTeacher1().getId(), null, slot.getStartTime(), slot.getEndTime()));
            schedulesMapper.insert(new Schedules(null, reviewId, assignment.getTeacher2().getId(), null, slot.getStartTime(), slot.getEndTime()));
            schedulesMapper.insert(new Schedules(null, reviewId, null, assignment.getRoom().getId(), slot.getStartTime(), slot.getEndTime()));
        }

        System.out.println(String.format("✅ 操作成功！共将 %d 条排程结果持久化到数据库。", finalAssignments.size()));
    }
}