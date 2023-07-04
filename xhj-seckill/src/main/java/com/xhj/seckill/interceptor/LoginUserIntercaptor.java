package com.xhj.seckill.interceptor;

import com.common.constant.AuthServerConstant;
import com.common.vo.MemberResponseVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:40
 * @Description: 拦截器
 */
@Component
public class LoginUserIntercaptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> threadLocal = new ThreadLocal<>();

    /**
     * 判断是否登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/kill", uri);
        if (match) {
            MemberResponseVo memberResponseVo = (MemberResponseVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
            if (memberResponseVo!=null){
                threadLocal.set(memberResponseVo);
                return true;
            }else{
                request.getSession().setAttribute("msg","请先进行登录");
                response.sendRedirect("http://auth.xhj.com/login.html");
                return false;
            }
        }
        return true;


    }
}
