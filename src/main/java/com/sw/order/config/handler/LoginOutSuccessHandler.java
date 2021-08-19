package com.sw.order.config.handler;

import com.sw.order.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 的登出逻辑
 * 自定义以json返回
 */
@Component
public class LoginOutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Result result = Result.ok("退出成功");
        httpServletResponse.getWriter().write(result.toJson());
    }
}
