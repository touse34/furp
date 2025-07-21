package com.furp.service;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;

import java.util.List;

public interface PhdInfoService {
    /**
     * 查询所有
     * @return
     */
    List<PhdInfo> findAll();
}
