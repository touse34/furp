package com.furp.service;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PhdService {
    /**
     * 查询所有
     * @return
     */
    List<Phd> findAll();



}
