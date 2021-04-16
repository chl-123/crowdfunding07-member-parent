package com.chl.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器访问地址
        String urlPath="/auth/member/to/register/page.html";
        //目标视图名称
        String viewName="member-register";
        //添加viewController
        registry.addViewController(urlPath).setViewName(viewName);
        urlPath="/auth/member/to/login/page.html";
        viewName="member-login";
        registry.addViewController(urlPath).setViewName(viewName);
        urlPath="/auth/member/to/center/page.html";
        viewName="member-center";
        registry.addViewController(urlPath).setViewName(viewName);
        urlPath="/auth/member/to/myCrowd/page.html";
        viewName="member-crowd";
        registry.addViewController(urlPath).setViewName(viewName);
    }
}
