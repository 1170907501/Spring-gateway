package com.zzh.handler;

import com.zzh.utils.JWTUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        // 从认证信息中获取用户名
        String username = authentication.getName();

        // 生成JWT Token
        String token = JWTUtils.generateToken(username,"");

        // 设置响应头或响应体
        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(authentication.getName()+"登录成功");
    }
}
