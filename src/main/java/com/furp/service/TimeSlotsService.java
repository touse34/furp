package com.furp.service;

import com.furp.DTO.TimeSlot;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.AvailableTime;
import com.furp.entity.TimeSlots;

import java.util.List;

public interface TimeSlotsService {
    public List<TimeConfigVO> getAvailableTimeSlots(Integer year);

    int updateDateConfigs(Integer year, List<TimeSlot> slots);
}


