package com.furp.service;

import com.furp.DTO.TeacherTimeSelectionDTO;
import com.furp.DTO.TimeSlot;
import com.furp.VO.AcademicTermVO;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.AvailableTime;
import com.furp.entity.TimeSlots;

import java.util.List;

public interface TimeSlotsService {
    public List<TimeConfigVO> getAvailableTimeSlots();

    int updateDateConfigs(List<TimeSlot> slots);

    public List<TimeConfigVO> getTeacherAvailableTimeSlots(Integer teacherId);

    //public List<AcademicTermVO> getAvailableAcademicTerms();

    int updateTeacherTimeSelection(Integer teacherId, TeacherTimeSelectionDTO selectedTime);

    //String getCurrentActiveTerm();

    List<TimeSlots> getAllTimeSlots();

    TimeSlot findTimeSlotById(Integer id);



}


