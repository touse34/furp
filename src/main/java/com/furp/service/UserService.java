package com.furp.service;

import com.furp.DTO.DashboardStatsDTO;
import com.furp.DTO.LoginDTO;
import com.furp.DTO.UserInfo;
import com.furp.entity.User;

import java.util.List;

public interface UserService {

    public User login(LoginDTO loginDTO);

    User getById(Integer id);

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 根据角色查询用户信息
     * @param roleId
     * @return
     */
    List<UserInfo> findByRole(Integer roleId);
    /**
     * 3.1 admin 获取Dashboard统计信息
     * @return
     */
    DashboardStatsDTO getStats();
}
