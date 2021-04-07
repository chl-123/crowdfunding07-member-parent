package com.chl.crowd.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/*
* 此方法用于
* */
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "short.message")
@Getter
@Setter
public class ShortMessageConfig {
    private String method ;
    private String appcode ;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }
}
