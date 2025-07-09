package com.furp.service.impl;

import com.furp.entity.AvailableTime;
import com.furp.mapper.AvailableTimeMapper;
import com.furp.service.AvailableTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvailableTimeImpl implements AvailableTimeService {
    @Autowired
    private AvailableTimeMapper availableTimeMapper;

    public Map<Integer, List<AvailableTime>> findAllAsMap(){
        List<Integer> teacherId = availableTimeMapper.findTeacherId();
        Map<Integer, List<AvailableTime>> theMap = new HashMap<>();
        for (Integer id : teacherId){
            List<AvailableTime> times = availableTimeMapper.findByTeacherId(id);
            theMap.put(id, times);
        }
        return theMap;
    }

}
