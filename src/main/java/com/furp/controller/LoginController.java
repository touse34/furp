package com.furp.controller;

import com.furp.DTO.LoginVo;
import com.furp.entity.Result;
import org.springframework.web.bind.annotation.PostMapping;

public class LoginController {


    @PostMapping("/login")
    public Result<LoginVo> login()
}
