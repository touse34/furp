package com.furp.DTO;

import com.furp.entity.Room;
import com.furp.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalAssignment {
    private PendingReviewDto reviewInfo; // 包含学生信息和 reviewId
    private Teacher teacher1;
    private Teacher teacher2;
    private Room room;
    private TimeSlot timeSlot;
}
