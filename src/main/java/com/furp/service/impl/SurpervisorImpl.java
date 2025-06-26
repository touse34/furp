package com.furp.service.impl;

import com.furp.entity.Supervisor;
import com.furp.mapper.SurpervisorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SurpervisorImpl {


    @Autowired
    private SurpervisorMapper surpervisorMapper;

    public List<Supervisor> findSupervisors(Integer phdId){
        return surpervisorMapper.findSupervisorsByPhdId(phdId);
    }
}
