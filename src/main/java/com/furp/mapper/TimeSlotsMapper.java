package com.furp.mapper;

import com.furp.DTO.TimeSlot;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.AvailableTime;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TimeSlotsMapper {
    @Select("SELECT id, start_time, end_time FROM time_slots WHERE is_active = 1 and academic_year = #{academicYear}")
    public List<TimeConfigVO> getAvailableTimeSlotsList(Integer academicYear);

    @Delete("DELETE FROM time_slots WHERE academic_year = #{academicYear}")
    public int deleteTimeSlotsByAcademicYear(Integer academicYear);

    void batchInsert(List<TimeSlot> slots);


}
