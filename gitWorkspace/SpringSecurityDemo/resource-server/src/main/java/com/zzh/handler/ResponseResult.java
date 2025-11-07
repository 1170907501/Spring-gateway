package com.zzh.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class ResponseResult {
    public static void exceptionResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403, \"message\":\"" + message + "\"}");
    }

    public static void exceptionResponse(HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403, \"message\":\"" + exception.getMessage() + "\"}");
    }

    public static void exceptionResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403, \"message\":\"" + authException.getMessage() + "\"}");
    }
}
