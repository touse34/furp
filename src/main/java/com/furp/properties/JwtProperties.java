package com.furp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.admin") // ① 匹配配置文件中 "jwt.admin" 前缀
@Data
public class JwtProperties {

    /**
     * 设置jwt签名加密时使用的秘钥
     */
    private String secretKey; // ② 对应 jwt.admin.secret-key

    /**
     * 设置jwt过期时间（毫秒）
     */
    private long ttl; // ③ 对应 jwt.admin.ttl

    /**
     * 设置前端传递过来的令牌名称
     */
    private String tokenName; // ④ 对应 jwt.admin.token-name

}
