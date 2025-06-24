package com.furp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.furp.mapper")
public class ReviewDistributionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewDistributionSystemApplication.class, args);
    }

}
