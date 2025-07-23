package com.furp.config;

import com.furp.intercepter.JwtUserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtUserTokenInterceptor jwtUserTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 定义需要排除的路径列表
        List<String> excludePatterns = new ArrayList<>();
        excludePatterns.add("/doc.html");              // Knife4j UI 页面
        excludePatterns.add("/webjars/**");            // 静态资源
        excludePatterns.add("/swagger-resources/**");  // Swagger 资源
        excludePatterns.add("/v3/api-docs/**");        // OpenAPI 规范数据 (非常重要!)
        excludePatterns.add("/favicon.ico");           // 网站图标
        excludePatterns.add("/api/scheduling/run");           // 网站图标
        registry.addInterceptor(jwtUserTokenInterceptor).addPathPatterns("/**").excludePathPatterns(excludePatterns);
    }
}
