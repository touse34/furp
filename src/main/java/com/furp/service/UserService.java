package com.furp.service;

import com.furp.DTO.LoginDTO;
import com.furp.entity.User;

public interface UserService {

    public User login(LoginDTO loginDTO);

    User getById(Integer id);

}
