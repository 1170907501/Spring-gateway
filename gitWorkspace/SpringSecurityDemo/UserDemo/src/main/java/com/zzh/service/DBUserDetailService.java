package com.zzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DBUserDetailService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 根据用户名可以从数据库获取用户信息，角色以及权限信息(一般设计用户信息表，角色表，权限表)
        // 模拟从数据库获取了用户信息，并封装成UserDetails对象
        UserDetails user = User
                .withUsername("jay")
                .password(passwordEncoder.encode("123456"))
                .roles("user")
                .build();
        return user;
    }
}