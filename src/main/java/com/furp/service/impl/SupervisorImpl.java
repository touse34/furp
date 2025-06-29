package com.furp.service.impl;

import com.furp.entity.Supervisor;
import com.furp.mapper.SupervisorMapper;
import com.furp.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SupervisorImpl implements SupervisorService {


    @Autowired
    private SupervisorMapper supervisorMapper;

    public List<Supervisor> findSupervisors(Integer phdId){
        return supervisorMapper.findSupervisorsByPhdId(phdId);
    }
}
