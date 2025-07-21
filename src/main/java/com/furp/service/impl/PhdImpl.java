package com.furp.service.impl;

import com.furp.entity.Phd;
import com.furp.mapper.AnnualReviewMapper;
import com.furp.mapper.PhdMapper;
import com.furp.service.PhdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhdImpl implements PhdService {

    /** 获取所有状态为pending的学生
     *
     *
     * */
    private AnnualReviewMapper annualReviewMapper;

    @Autowired
    private PhdMapper phdMapper;

    @Override
    public List<Phd> findAll() {
        return phdMapper.findAll();
    }
}
