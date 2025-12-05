package com.auto.u2a.polit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 企业级用户平台驾驶舱系统 - 启动类
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class AutoU2aPolitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoU2aPolitApplication.class, args);
    }

}