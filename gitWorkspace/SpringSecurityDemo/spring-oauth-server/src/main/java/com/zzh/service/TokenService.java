package com.zzh.service;

import com.zzh.dto.TokenPair;
import com.zzh.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

// Token服务类
@Service
public class TokenService {
    public TokenPair refreshTokenPair(Claims claims) {
        String username = claims.getSubject();
        // 生成新的Token对
        String newAccessToken = JWTUtils.generateToken(username, "");
        String newRefreshToken = JWTUtils.generateToken(username, "");
        return new TokenPair(newAccessToken, newRefreshToken);
    }
}
