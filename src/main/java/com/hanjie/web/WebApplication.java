package com.hanjie.web;

import com.hanjie.web.interceptor.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;



@SpringBootApplication
//@EnableScheduling
//@MapperScan("com.hanjie.web.dao")
//@ComponentScan
//@EnableAutoConfiguration
@EnableScheduling
//@Configuration
public class WebApplication  {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}
