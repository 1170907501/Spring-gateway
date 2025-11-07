
package com.zzh.controller;

import com.zzh.service.DBUserDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private DBUserDetailService dbUserDetailService;

    @PostMapping("/user/login")
    public void demo() {
        dbUserDetailService.loadUserByUsername("jay");
    }
    @PostMapping("/login")
    public void demoLogin() {
        dbUserDetailService.loadUserByUsername("jay");
    }
}

