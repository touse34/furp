package com.furp.service;

import com.furp.DTO.PhdUserInfo;

import java.util.List;

public interface PhdUserInfoService {

    /**
     * 获取所有 PhD 的信息，包括所选的导师信息
     * @return
     */
    List<PhdUserInfo> getAllPhdWithSupervisors();
}
