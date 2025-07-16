package com.furp.controller;

import com.furp.DTO.LoginDTO;
import com.furp.DTO.LoginVo;
import com.furp.entity.Result;
import com.furp.entity.User;
import com.furp.mapper.UserMapper;
import com.furp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;



    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDTO loginDTO){
        log.info("登录：{}", loginDTO);

        User user = userService.login(loginDTO);






    }
}
