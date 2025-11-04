package com.zzh.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    // 密钥（生产环境应从安全配置读取）
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 自动生成256位密钥
    private static final String ISSUER = "user-auth-service";
    private static final long EXPIRATION_TIME = 3600000; // 1小时（毫秒）

    /**
     * 解析JWT并获取所有声明
     * @param token JWT字符串
     * @return 包含所有声明的Map（Key为声明名，Value为对象）
     * @throws JwtException 当token无效/过期时抛出异常
     */
    public static Map<String, Object> parseToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return claims.getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Token解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查JWT是否在有效期内（基于exp声明）
     * @param expiresIn 预期的过期时间（秒）
     * @return 是否有效（未过期）
     */
    public static boolean isExpiresIn(long expiresIn) {
        long now = System.currentTimeMillis();
        long expirationTimeMillis = expiresIn * 1000; // 转为毫秒
        return now < expirationTimeMillis;
    }

    /**
     * 根据用户名生成标准JWT Token
     * @param username 用户唯一标识（如邮箱/手机号）
     * @param audience 目标接收方（可选）
     * @return 符合RFC 7519标准的JWT字符串
     */
    public static String generateToken(String username, String audience) {
        // 定义自定义声明（可扩展用户角色/权限）
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("roles", new String[]{"USER", "MEMBER"}); // 示例角色

        // 构建Token
        return Jwts.builder()
                .setHeaderParams(getHeaders()) // 设置Header
                .setClaims(customClaims)       // 添加自定义声明
                .setSubject(username)          // 设置sub标准声明
                .setIssuer(ISSUER)             // 设置iss标准声明
                .setAudience(audience)         // 设置aud标准声明
                .setIssuedAt(new Date())       // 设置iat标准声明
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置exp
                .signWith(SECRET_KEY)          // 使用HS512签名
                .compact();
    }

    /**
     * 获取JWT Header标准配置
     * @return 包含alg和typ的Header参数
     */
    private static Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS512");
        headers.put("typ", "JWT");
        return headers;
    }

    // 示例调用
    public static void main(String[] args) {
        String token = generateToken("user@example.com", "web-client");
        System.out.println("Generated JWT:\n" + token);
    }
}