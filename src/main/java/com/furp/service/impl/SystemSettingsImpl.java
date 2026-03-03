package com.furp.service.impl;

import com.furp.DTO.DeadlineVO;
import com.furp.mapper.SystemSettingMapper;
import com.furp.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemSettingsImpl implements SystemSettingsService {
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    @Override
    public DeadlineVO getDeadlineInfo() {
        LocalDateTime deadLine = systemSettingMapper.getDeadline();
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isOpen = (deadLine != null && currentTime.isBefore(deadLine));

        DeadlineVO vo = new DeadlineVO();
        vo.setDeadline(deadLine);
        vo.setCurrentTime(currentTime);
        vo.setIsOpen(isOpen);

        return vo;
    }
}
