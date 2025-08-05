package com.furp.service.impl;

import com.furp.DTO.TimeSlot;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.TimeSlots;
import com.furp.mapper.TimeSlotsMapper;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotsImpl implements TimeSlotsService {
    @Autowired
    private TimeSlotsMapper timeSlotsMapper;

    @Override
    public List<TimeConfigVO> getAvailableTimeSlots(Integer year) {
        return timeSlotsMapper.getAvailableTimeSlotsList(year);
    }

    @Override
    public int updateDateConfigs(Integer year, List<TimeSlot> slots) {
        timeSlotsMapper.deleteTimeSlotsByAcademicYear(year);

        // 如果传入的 slots 为空，直接返回 0
        if (slots == null || slots.isEmpty()) {
            return 0;
        }

        timeSlotsMapper.batchInsert(slots);

        // 调用 mapper 方法更新数据库中的时间段配置
        return slots.size();
    }
}
