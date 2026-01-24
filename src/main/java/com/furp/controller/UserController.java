package com.furp.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.furp.DTO.*;
import com.furp.entity.Phd;
import com.furp.entity.Result;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.TeacherMapper;
import com.furp.service.PhdUserInfoService;
import com.furp.service.StudentAddService;
import com.furp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private PhdMapper phdMapper;
    @Autowired
    private PhdUserInfoService phdUserInfoService;


/*    @GetMapping("/admin/users")
    public Result getUsersInfo(){
        System.out.println("查询全部用户信息");
        List<UserInfo> userList = userService.findAll();
        return Result.success(userList);

    }*/
@SaCheckRole("admin")
@GetMapping("/admin/{roleId}")
public Result listUsers(@PathVariable Integer roleId){
       /* System.out.println("根据roleId来查询用户");
        List<UserInfo> userInfoList = userService.findByRole(roleId);
        return Result.success(userInfoList);*/

    if(roleId==1){
        List<UserInfo> teachers = userService.findByRole(1);
        return Result.success(teachers);
    } else if (roleId==2) {
        List<PhdUserInfo> phds = phdUserInfoService.getAllPhdWithSupervisors();
        return Result.success(phds);

    }else{
        return Result.error("暂不支持的 roleId = " + roleId);
    }

}




    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDTO loginDTO){
        log.info("登录：{}", loginDTO);

        User user = userService.login(loginDTO);

        StpUtil.login(user.getId());

        SaSession session = StpUtil.getSession();

        // 2. 使用 switch 表达式来构建 LoginVo 对象
        LoginVo loginVo = switch (user.getRoleId()) {
            case 1 -> {
                Teacher teacher = teacherMapper.selectTeacherByUserId(user.getId());
                if (teacher == null) throw new RuntimeException("教师信息不存在");
                session.set("role", "teacher");
                session.set("teacherId", teacher.getId());
                yield LoginVo.builder().userId(user.getId())
                        .role("teacher")
                        .name(teacher.getName())
                        .teacherId(teacher.getId())
                        .build();
            }
            case 2 -> {
                Phd phd = phdMapper.selectPhdByUserId(user.getId());
                if (phd == null) throw new RuntimeException("博士生信息不存在");
                session.set("role", "phd");
                session.set("phdId", phd.getId());
                yield LoginVo.builder().userId(user.getId())
                        .role("phd")
                        .name(phd.getName())
                        .phdId(phd.getId())
                        .build();
            }
            case 3 -> {
                session.set("role", "admin");
                yield LoginVo.builder().userId(user.getId())
                    .role("admin")
                    .name("admin")
                    .build();
            }
            default -> throw new RuntimeException("无效的用户角色: " + user.getRoleId());
        };

        String token = StpUtil.getTokenValue();

        loginVo.setToken(token);

        return Result.success(loginVo);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.success("登出成功");
    }

}

