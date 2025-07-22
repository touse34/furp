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

    /**
     * 修改phdskill
     * @param phdInfo
     */
    void update(PhdInfo phdInfo);


    /**
     * 根据id查询phd信息
     * @param studentId
     * @return
     */
    PhdInfo getById(Integer studentId);
}
