package com.chl.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/agree/protocol/page.html").setViewName("project-agree");
        registry.addViewController("/launch/project/page.html").setViewName("project-launch");
    }

}
