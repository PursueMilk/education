package com.example.controller;


import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.commonutils.user.UcenterMemberOrder;
import com.example.entity.UcenterMember;
import com.example.entity.vo.RegisterVo;
import com.example.service.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-08-09
 */
@Slf4j
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //电话和密码登录
    @PostMapping("login")
    public R login(@RequestBody UcenterMember ucenterMember) {
        String token = ucenterMemberService.login(ucenterMember);
        return R.ok().data("token", token);
    }

    //电话、密码、验证码、昵称进行注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo) {
        log.info("{}", registerVo);
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类，获取头部信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("用户名称" + memberId);
        //查询数据库根据id获得用户信息
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    //根据用户id获取客户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        //把UcenterMember复制为UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember, ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day) {
        Integer count = ucenterMemberService.ucenterMemberService(day);
        return R.ok().data("countRegister", count);
    }

}

