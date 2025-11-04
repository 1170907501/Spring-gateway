package com.zzh.filter;

import com.zzh.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 排除放行路径
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 1.从请求头中取出token，进行判断，如果没有携带token，则继续往下走其他的其他的filter逻辑
        String tokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(tokenValue)) { //如果没有token值则拦截
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":401, \"msg\":\"Token required\"}");
            return; // 终止请求
        }
        // 2. 校验token
        // 2.1 将token切割前缀“bearer ”，然后使用封装的JWT工具解析token，得到一个map对象
        String token = tokenValue.substring("bearer ".length());
        Map<String, Object> map = JWTUtils.parseToken(token);
        // 2.2 取出token中的过期时间，调用JWT工具中封装的过期时间校验，如果token已经过期，则删除登录的用户，继续往下走其他filter逻辑
        if (JWTUtils.isExpiresIn((long) map.get("expiresIn"))) {
            // token 已经过期
            SecurityContextHolder.getContext().setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }

        String username = (String) map.get("username");
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 获取用户信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置用户登录状态
                System.out.println("authenticated user {}, setting security context"+username );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
    // 定义放行路径
    private boolean isPublicPath(String uri) {
        return uri.equals("/login.html") || uri.equals("/user/login");
    }
}
