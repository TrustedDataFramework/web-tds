package com.hanjie.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



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
