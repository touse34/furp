package com.furp.service.impl;

import com.furp.DTO.PhdInfo;
import com.furp.entity.PhdSkill;
import com.furp.mapper.PhdInfoMapper;
import com.furp.mapper.PhdMapper;
import com.furp.service.PhdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhdInfoImpl implements PhdInfoService {

    @Autowired
    private PhdInfoMapper phdInfoMapper;
    @Autowired
    private PhdMapper phdMapper;

    @Override
    public List<PhdInfo> findAll() {
        return phdInfoMapper.findAll();
    }



    @Override
    public PhdInfo getById(Integer studentId) {
        return phdInfoMapper.getById(studentId);
    }
}
