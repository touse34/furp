package com.furp.service.impl;

import com.furp.DTO.LoginDTO;
import com.furp.DTO.UserInfo;
import com.furp.entity.User;
import com.furp.exception.AccountNotFoundException;
import com.furp.exception.PasswordErrorException;
import com.furp.mapper.UserMapper;
import com.furp.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    public User login(LoginDTO loginDTO){
        String username = loginDTO.getUsername();
        String rawPassword = loginDTO.getPassword();

        User user = userMapper.getUserByName(username);

        if(user == null){
            //账号不存在
            throw new AccountNotFoundException("用户名不存在");
        }
        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            // 密码错误
            throw new PasswordErrorException("密码错误");
        }

        return user;

    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<UserInfo> findAll() {
        return userMapper.findAll();
    }
}
