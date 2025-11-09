package com.zzh.service;

import com.zzh.entity.SysUserEntity;

public interface SysUserService {
    /**
     *
     * @param username
     * @return
     * @author  Rommel
     * @date    2023/7/12-23:48
     * @version 1.0
     * @description  根据用户名查询用户信息
     */
    SysUserEntity selectByUsername(String username);
}
