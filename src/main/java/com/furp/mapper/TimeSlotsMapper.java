package com.furp.mapper;

import com.furp.DTO.TimeSlot;
import com.furp.VO.TimeConfigVO;
import com.furp.entity.TimeSlots;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TimeSlotsMapper {
    @Select("SELECT id, start_time, end_time FROM time_slots WHERE is_active = 1")
    List<TimeConfigVO> getAvailableTimeSlotsList();

    @Delete("DELETE FROM time_slots")
    void deleteAllTimeSlots();

    void batchInsert(List<TimeSlot> slots);

//    @Select("SELECT DISTINCT academic_year FROM time_slots " +
//            "ORDER BY " +
//            "    SUBSTRING(academic_year, 1, 4) DESC, " + // 1. 先按年份数字降序
//            "    CASE " +
//            "        WHEN academic_year LIKE '%-Fall' THEN 2 " + // 2. Fall 在后，给一个较大的值
//            "        WHEN academic_year LIKE '%-Spring' THEN 1 " +// 3. Spring 在前
//            "        ELSE 0 " +
//            "    END DESC") // 4. 然后按季节的代表数字降序
//    List<String> findDistinctAcademicYears();

    @Select("SELECT * FROM time_slots WHERE is_active = 1")
    List<TimeSlots> findActiveSlots();

    @Select("SELECT id, start_time, end_time ,is_active FROM time_slots ORDER BY start_time ASC")
    List<TimeSlots> getAllTimeSlots();

    @Select("SELECT start_time, end_time FROM time_slots where id = #{id}")
    TimeSlot findBeginEndTimeById(Integer id);
}
