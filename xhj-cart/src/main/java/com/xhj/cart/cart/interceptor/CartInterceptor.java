package com.xhj.cart.cart.interceptor;

import com..common.constant.AuthServerConstant;
import com..common.constant.CartConstant;
import com..common.vo.MemberResponseVo;
import com.xhj.cart.cart.vo.UserInfoTo;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @Author: xhj
 * @Date: 2023/05/23/14:55
 * @Description:
 */

public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();

        HttpSession session = request.getSession();
        MemberResponseVo member = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member != null){
            // 登录了
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CartConstant.TEMP_USER_COOKIS_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setIsTempUser(true);
                }
            }
        }
        //如果没有临时用户一定分配一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 方法执行之后，
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();

        if (!userInfoTo.getIsTempUser()){
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIS_NAME,userInfoTo.getUserKey());
            cookie.setDomain("xhj.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIS_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
