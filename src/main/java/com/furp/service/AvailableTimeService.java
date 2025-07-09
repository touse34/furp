package com.furp.service;

import com.furp.entity.AvailableTime;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AvailableTimeService {
    public Map<Integer, List<AvailableTime>> findAllAsMap();
}
