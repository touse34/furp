package com.furp.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UpdateTimeSlotsDTO {

    private String academicYear; // 学年

    private List<TimeSlot> Slots; // 时间段列表
}
