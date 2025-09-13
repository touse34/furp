package com.furp.service;

import com.furp.DTO.*;
import com.furp.VO.UserAddResponseVO;
import com.furp.entity.User;
import com.furp.response.PageResult;

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
    /**
     4.1 获取用户列表
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);


    /**
     4.2 添加用户
     */
    UserAddResponseVO addUser(UserAddDTO userAddDTO);
}
