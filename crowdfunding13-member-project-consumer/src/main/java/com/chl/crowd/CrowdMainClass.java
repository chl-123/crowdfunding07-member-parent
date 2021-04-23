package com.chl.crowd;

import com.chl.crowd.config.MultipartConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class  CrowdMainClass extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass.class,args);
    }
}
