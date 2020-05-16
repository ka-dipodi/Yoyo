package com.fgy.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 登录拦截器
* */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /*重写预处理方法*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*获取Session 判断里面是否存在user(有登录则存在，否则不存在)*/
        if(request.getSession().getAttribute("user")==null){
            /*重定向至登陆页面*/
            response.sendRedirect("/admin");
            return false;//停止运行
        }
        return true;//继续运行
    }
}
