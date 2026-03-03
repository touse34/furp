package com.furp.mapper;

import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface SystemSettingMapper {

    @Select("SELECT time_selection_deadline FROM system_settings WHERE id = 1")
    LocalDateTime getDeadline();
}
