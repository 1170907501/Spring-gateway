package com.zzh.filter;
import com.zzh.dto.TokenPair;
import com.zzh.service.DBUserDetailService;
import com.zzh.service.TokenService;
import com.zzh.utils.JWTUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

@Component
public class JwtRefreshFilter extends OncePerRequestFilter {

    // 自动生成HS512算法密钥（生产环境应从配置中心获取）
    private static final long REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000; // 7天
    private static final long REFRESH_THRESHOLD = 5 * 60 * 1000; // 提前5分钟续期

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    @Lazy
    private DBUserDetailService dbUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String token = extractToken(request);
        if (token != null) {
            try {
                // 1. 解析并验证Token
                Claims claims = (Claims) JWTUtils.parseToken(token);

                // 2. 检查是否需要续期
                if (shouldRefresh(claims)) {
                    // 3. 生成新Token对
                    TokenPair newTokens = tokenService.refreshTokenPair(claims);

                    // 4. 设置新Token到响应头
                    response.setHeader("Authorization", "Bearer " + newTokens.getAccessToken());
                    response.setHeader("X-Refresh-Token", newTokens.getRefreshToken());

                    // 5. 更新Redis存储（如果使用）
                    storeTokenInRedis(newTokens.getAccessToken(), newTokens.getRefreshToken());
                }

                // 6. 将认证信息存入SecurityContext
                setAuthentication(claims.getSubject());

            } catch (JwtException e) {
                // 异常处理：Token无效或过期
                clearAuthentication(request);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


    private boolean shouldRefresh(Claims claims) {
        long now = System.currentTimeMillis();
        Date exp = claims.getExpiration();
        long refreshTime = exp.getTime() - REFRESH_THRESHOLD;
        return now > refreshTime;
    }

    private void setAuthentication(String username) {
        // 实际应用中应从数据库加载用户详情
        UserDetails userDetails = dbUserDetailService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void storeTokenInRedis(String accessToken, String refreshToken) {
        String tokenHash = DigestUtils.sha256Hex(accessToken);
        // 设置Redis存储（示例）
        redisTemplate.opsForValue().set(
                "refresh_token:" + tokenHash,
                refreshToken,
                REFRESH_TOKEN_EXPIRE,
                TimeUnit.MILLISECONDS);
    }

    /**
     * 清除当前安全上下文（包括Token过期/无效场景）
     * 遵循"深度清理"原则确保无残留认证信息
     */
    public static void clearAuthentication(HttpServletRequest request) {
        // 1. 清除线程绑定安全上下文（核心操作）
        SecurityContextHolder.clearContext();

        // 2. 可选：清除HTTP会话中的安全上下文（防止会话绑定残留）
        // 适用于使用Session存储SecurityContext的场景
        request.getSession().removeAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
        );
    }
}
