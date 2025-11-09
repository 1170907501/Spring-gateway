package com.zzh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zzh.mapper")  // 只扫描mapper包
public class SpringOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringOauthServerApplication.class, args);
    }
}
