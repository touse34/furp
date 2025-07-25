package com.furp.controller;

import com.furp.DTO.LoginDTO;
import com.furp.DTO.LoginVo;
import com.furp.DTO.PhdUserInfo;
import com.furp.DTO.UserInfo;
import com.furp.entity.Phd;
import com.furp.entity.Result;
import com.furp.entity.Teacher;
import com.furp.entity.User;
import com.furp.mapper.PhdMapper;
import com.furp.mapper.PhdUserInfoMapper;
import com.furp.mapper.TeacherMapper;
import com.furp.properties.JwtProperties;
import com.furp.service.PhdUserInfoService;
import com.furp.service.UserService;
import com.furp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
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

    @GetMapping("/admin/users/{roleId}")
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

        Map<String, Object> claim = new HashMap<>();
        claim.put("user_id", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claim);
//初版
//        switch (user.getRoleId()){
//            case 1 :
//                Teacher teacher = teacherMapper.selectTeacherByUserId(user.getId());
//
//                LoginVo vo1 = LoginVo.builder().userId(user.getId())
//                        .role("teacher")
//                        .name(teacher.getName())
//                        .teacherId(teacher.getId())
//                        .build();
//
//                return Result.success(vo1);
//
//
//            case 2 :
//                Phd phd = phdMapper.selectPhdByUserId(user.getId());
//                LoginVo vo2 = LoginVo.builder().userId(user.getId())
//                        .role("phd")
//                        .name(phd.getName())
//                        .phdId(phd.getId())
//                        .build();
//
//                return Result.success(vo2);
//
//            case 3 :
//                LoginVo vo3 = LoginVo.builder().userId(user.getId())
//                        .role("admin")
//                        .name("admin")
//                        .build();
//
//                return Result.success(vo3);
//        }

//ai改进版

        // 2. 使用 switch 表达式来构建 LoginVo 对象
        LoginVo loginVo = switch (user.getRoleId()) {
            case 1 -> {
                Teacher teacher = teacherMapper.selectTeacherByUserId(user.getId());
                yield LoginVo.builder().userId(user.getId())
                        .role("teacher")
                        .name(teacher.getName())
                        .teacherId(teacher.getId())
                        .build();
            }
            case 2 -> {
                Phd phd = phdMapper.selectPhdByUserId(user.getId());
                yield LoginVo.builder().userId(user.getId())
                        .role("phd")
                        .name(phd.getName())
                        .phdId(phd.getId())
                        .build();
            }
            case 3 -> LoginVo.builder().userId(user.getId())
                    .role("admin")
                    .name("admin")
                    .build();
            default -> null; // 或者抛出异常 throw new RuntimeException("无效角色");
        };

        // 3. 统一处理返回结果
        if (loginVo == null) {
            return Result.error("无效的用户角色");
        }

        // 4. 将 token 设置到 vo 中 (见下方说明)
        loginVo.setToken(token);

        return Result.success(loginVo);
    }

}

