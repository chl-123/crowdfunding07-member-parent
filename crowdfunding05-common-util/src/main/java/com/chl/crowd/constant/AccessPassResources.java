package com.chl.crowd.constant;

import java.util.HashSet;
import java.util.Set;
/*
* 该工具类方法是用来准备不需要登录检查的资源
* */
public class AccessPassResources {
    //放行的地址
    public static final Set<String> PASS_RES_SET=new HashSet<String>();
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/register/page.html");
        PASS_RES_SET.add("/auth/do/member/register.html");
        PASS_RES_SET.add("/auth/member/to/login/page.html");
        PASS_RES_SET.add("/auth/do/member/login.html");
        PASS_RES_SET.add("/auth/member/do/loginout.html");
        PASS_RES_SET.add("/auth/member/send/short/message.json");

    }
    //放行的静态资源
    public static final Set<String> STATIC_RES_SET=new HashSet<String>();
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }
    /*
    * 判断某个地址是否是在静态资源里面
    * */
    public static boolean judgeCurrentServletPathWitherStaticResources(String servletPath){
        //先判断字符串是否合法
        if (servletPath == null||servletPath.length()==0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        //将字符串进行切割
        String[] split = servletPath.split("/");
        //第一个“/"左边是空字符串，所以取第二个
        String firstLevelPath = split[1];
        return STATIC_RES_SET.contains(firstLevelPath);
    }


}
