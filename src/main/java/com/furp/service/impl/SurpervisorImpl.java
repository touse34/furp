package com.furp.service.impl;

import com.furp.entity.Supervisor;
import com.furp.mapper.SupervisorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SurpervisorImpl {


    @Autowired
    private SupervisorMapper supervisorMapper;

    public List<Supervisor> findSupervisors(Integer phdId){
        return supervisorMapper.findSupervisorsByPhdId(phdId);
    }
}
