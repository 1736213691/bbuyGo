package com.sw.order.config.handler;

import com.sw.order.entity.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 登录成功逻辑
 * 自定义以json数据返回用户信息
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 跨域处理
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的请求方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST");
        // 允许的请求头
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // 返回
        Result result = new Result(200,"登录成功");
        result.put("user", httpServletRequest.getSession().getAttribute("user"));
        httpServletResponse.getWriter().write(result.toJson());
    }
}
