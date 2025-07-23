package com.furp.service;

import com.furp.DTO.FinalAssignment;

import java.util.List;

public interface SchedulingPersistenceService {
    public void persistSchedule(List<FinalAssignment> finalAssignments);
}
