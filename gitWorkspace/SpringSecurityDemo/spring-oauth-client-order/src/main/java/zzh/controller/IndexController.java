package zzh.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/index")
    public String index(){
        System.out.println("index,无需登录与授权");
        return "index,无需登录与授权";
    }

    @GetMapping("order1")
    @RolesAllowed({"ROLE_user","ROLE_admin"}) // 配置访问此方法时应该具有的角色
    public String order1(){
        System.out.println("order1需要登录和授权");
        return "order1需要登录和授权";
    }
}
