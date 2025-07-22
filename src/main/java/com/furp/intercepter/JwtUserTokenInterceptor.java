package com.furp.intercepter;

import com.furp.properties.JwtProperties;
import com.furp.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
* 令牌校验拦截器
* */
@Slf4j
@Component
public class JwtUserTokenInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public JwtUserTokenInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String requestURI = request.getRequestURI();

       if (requestURI.contains("/login")){
           log.info("登录请求,放行");
           return true;
       }

       String token = request.getHeader("token");
       if (token == null || token.isEmpty()){
           log.info("令牌非法");
           response.setStatus(401);
           return false;
       }

        try {

            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Integer userId = (Integer)claims.get("user_id");
            System.out.println("id是"+userId);
            request.setAttribute("currentUserId", userId);

        } catch (Exception e){
            log.info("令牌验证不通过");
            response.setStatus(401);
            return false;
        }

        log.info("校验通过，放行");
        return true;
    }
}
