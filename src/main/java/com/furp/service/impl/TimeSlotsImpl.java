package com.furp.service.impl;

import com.furp.DTO.TeacherTimeSelectionDTO;
import com.furp.DTO.TimeSlot;
import com.furp.VO.AcademicTermVO;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.TimeSlots;
import com.furp.mapper.AvailableTimeMapper;
import com.furp.mapper.TimeSlotsMapper;
import com.furp.service.TimeSlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TimeSlotsImpl implements TimeSlotsService {
    @Autowired
    private TimeSlotsMapper timeSlotsMapper;
    @Autowired
    private AvailableTimeMapper availableTimeMapper;

    @Override
    public List<TimeConfigVO> getAvailableTimeSlots(String year) {
        return timeSlotsMapper.getAvailableTimeSlotsList(year);
    }

    @Override
    public int updateDateConfigs(String year, List<TimeSlot> slots) {
        timeSlotsMapper.deleteTimeSlotsByAcademicYear(year);

        // 如果传入的 slots 为空，直接返回 0
        if (slots == null || slots.isEmpty()) {
            return 0;
        }

        timeSlotsMapper.batchInsert(slots);

        // 调用 mapper 方法更新数据库中的时间段配置
        return slots.size();
    }

    @Override
    public List<TimeConfigVO> getTeacherAvailableTimeSlots(String year, Integer teacherId) {
        List<TimeSlots> allAvailableSlots = timeSlotsMapper.findActiveSlotsByYear(year);
        if (allAvailableSlots.isEmpty()) {
            return Collections.emptyList(); // 如果管理员没配置，直接返回空列表
        }

        // 2. 获取该老师“已选”的时间段ID集合
        Set<Integer> selectedSlotIds = availableTimeMapper.findSelectedSlotIdsByTeacherAndYear(teacherId, year);

        // 3. 在内存中进行组装，生成最终的VO列表
        return allAvailableSlots.stream()
                .map(slot -> new TimeConfigVO(
                        slot.getId(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        // 核心逻辑：如果老师的“已选”集合中包含当前时间段的ID，则'selected'为true
                        selectedSlotIds.contains(slot.getId())
                ))
                .collect(Collectors.toList());

    }

    public List<AcademicTermVO> getAvailableAcademicTerms(){
        List<String> yearsFromDb = timeSlotsMapper.findDistinctAcademicYears();
        String currentTerm = determineCurrentTerm();
        // 3. 在内存中进行组装
        return yearsFromDb.stream()
                .map(yearStr -> new AcademicTermVO(
                        yearStr,
                        yearStr.equals(currentTerm) // 判断是否为当前默认
                ))
                .collect(Collectors.toList());
    }

    @Override
    public int updateTeacherTimeSelection(Integer teacherId, TeacherTimeSelectionDTO selectedTime) {
        List<Integer> selectedTimeIds = selectedTime.getSlotIds();
        String year = selectedTime.getAcademicYear();
        // 1. 删除该教师之前的所有时间段选择
        availableTimeMapper.deleteByTeacherAndYear(teacherId, year);
        // 2. 如果没有选择任何时间段，直接返回 0
        if (selectedTimeIds == null || selectedTimeIds.isEmpty()) {
            return 0;
        }
        // 3. 批量插入新的时间段选择

        availableTimeMapper.batchInsertFromTimeSlots(teacherId, selectedTimeIds);
        // 4. 返回插入的数量
        return selectedTimeIds.size();
    }

    /**
     * 【核心业务逻辑】根据当前日期确定当前的学年/评审季
     * @return 代表当前学期的字符串, e.g., "2025-Spring"
     */
    private String determineCurrentTerm() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        Month currentMonth = today.getMonth();

        // 根据月份判断是Spring季还是Fall季
        if (currentMonth.getValue() >= 1 && currentMonth.getValue() <= 7) {
            // 1月到7月，属于当年的Spring季
            return currentYear + "-Spring";
        } else {
            // 8月到12月，属于当年的Fall季
            return currentYear + "-Fall";
        }
    }
}
