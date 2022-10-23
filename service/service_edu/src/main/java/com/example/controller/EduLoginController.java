package com.example.controller;


import com.example.commonutils.R;
import org.springframework.web.bind.annotation.*;


/**
 * 后阶段由权限控制管理登录
 */
@RestController
@RequestMapping("/eduservice/user")
public class EduLoginController {

    //login
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif").data("introduction","简介");
    }
}
