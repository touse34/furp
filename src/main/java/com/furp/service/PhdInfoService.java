package com.furp.service;

import com.furp.DTO.PhdInfo;
import com.furp.entity.Phd;
import com.furp.entity.PhdSkill;

import java.util.List;

public interface PhdInfoService {
    /**
     * 查询所有
     * @return
     */
    List<PhdInfo> findAll();




    /**
     * 根据id查询phd信息
     * @param studentId
     * @return
     */
    PhdInfo getById(Integer studentId);
}
