package com.chl.crowd.filter;

import com.chl.crowd.constant.AccessPassResources;
import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.entity.Member;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class CrowdAccessFilter extends ZuulFilter {
    public String filterType() {
        //在目标微服前执行过滤
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        //获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //通过RequestContext对象获取当前请求对象(框架底层是借助 ThreadLocal 从当前
        //线程上获取事先绑定的 Request 对象
        HttpServletRequest request = requestContext.getRequest();
        //获取servletPath的值
        String servletPath = request.getServletPath();
        //根据servletPath判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (containsResult) {
            //如过当前请求是特定的功能则返回false放行
            return false;
        }
        //判断是否为静态资源
        boolean staticResourcesResult = AccessPassResources.judgeCurrentServletPathWitherStaticResources(servletPath);
        if (staticResourcesResult) {
            //如果为静态资源则放行
            return false;
        }
        return true;
    }

    public Object run() throws ZuulException {
        //获取当前请求对象
        RequestContext requestContext=RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //获取当前Session对象
        HttpSession session = request.getSession();
        //尝试从Session对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        //判断loginMember是否为空
        if (loginMember == null) {
            //从requestContext对象中获取Response对象
            HttpServletResponse response = requestContext.getResponse();
            //将提示消息存入Session域中
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            //重定向到登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
