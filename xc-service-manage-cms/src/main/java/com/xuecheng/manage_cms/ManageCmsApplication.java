package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EntityScan("com.xuecheng.domain.cms")
@ComponentScan("com.xuecheng.api")
@ComponentScan("com.xuecheng.manage_cms")
@ComponentScan("com.xuecheng.framework")
public class ManageCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class, args);
    }
}
