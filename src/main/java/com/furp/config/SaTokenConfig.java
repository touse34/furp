package com.furp.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 的登录拦截器
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(    // -- 放行路径 --
                        "/users/login",                // 登录接口
                        "/doc.html",                   // Knife4j UI 页面
                        "/webjars/**",                 // 静态资源
                        "/swagger-resources/**",       // Swagger 资源
                        "/v3/api-docs/**",             // OpenAPI 规范数据
                        "/favicon.ico",                // 网站图标
                        "/api/scheduling/run"         // 自动排程接口
                        //"/admin/dashboard/stats",
                        //"/admin/users"

                );
    }
}