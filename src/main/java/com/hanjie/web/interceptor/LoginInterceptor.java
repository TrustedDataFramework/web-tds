package com.hanjie.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        //已经登录了
        if (null != user){
            //放行，到下一个方法
            return true;
        }
        log.info("拦截地址：{}",request.getRequestURL());
        //验证不通过
//        request.setAttribute("msg","您还没登陆，请先登录后再操作");
//        request.getRequestDispatcher("/web/login").forward(request,response);
          request.getRequestDispatcher("/index.html").forward(request, response);
//        response.sendRedirect(request("/web/login"));

        return false;
        //此时还没生效，必须注册到容器中
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        preHandle(request,response,handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        preHandle(request,response,handler);
    }
}
