package com.furp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Knife4j 配置类
 *
 * @author YourName
 */
@Configuration
public class Knife4jConfig {

    /**
     * 配置一个名为 "default" 的 API 分组
     * 用于定义 API 的扫描范围和分组信息
     */
    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                // 分组名称，会显示在UI上
                .group("default")
                // 匹配所有以 /api/ 开头的路径
                .pathsToMatch("/api/**")
                // 指定要扫描的 Controller 包
                .packagesToScan("com.example.project.controller")
                .build();
    }

    /**
     * 配置一个名为 "admin" 的 API 分组
     * 这是一个示例，用于演示如何创建多个分组
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-group")
                .pathsToMatch("/admin/**")
                .packagesToScan("com.example.project.admin")
                .build();
    }


    /**
     * 配置 OpenAPI 的基本信息和安全认证
     * 用于定义整个文档的元数据，如标题、描述、版本、联系方式以及安全方案
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 定义认证方案的名称
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("项目 API 文档")
                        .description("这是一个基于 Knife4j 和 SpringDoc OpenAPI 3 构建的示例 API 文档。")
                        .version("v1.0.0")
                        .contact(new Contact().name("Your Name").email("your.email@example.com").url("https://example.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                // 添加全局的安全认证要求
                // 这将在每个 API 端点的右上角显示一个锁图标，表明它需要认证
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 配置组件，定义安全方案
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP) // 认证类型为 HTTP
                                        .scheme("bearer") // 认证方案为 Bearer Token
                                        .bearerFormat("JWT") // Bearer 格式为 JWT
                        )
                );
    }
}
