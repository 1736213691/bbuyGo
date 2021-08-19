package com.sw.order.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.order.entity.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 的用户认证失败逻辑
 * 自定义以json方式返回
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // 返回值
        httpServletResponse.getWriter().write(Result.clientError(999,"登录失败").toJson());
    }
}
