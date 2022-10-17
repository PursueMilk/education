package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.MD5;
import com.example.entity.UcenterMember;
import com.example.entity.vo.RegisterVo;
import com.example.exceptionhandler.GuliException;
import com.example.mapper.UcenterMemberMapper;
import com.example.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-09
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    //用来验证，redis验证码是否匹配
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember ucenterMember) {
        //手机号和密码做登录
        //1、获取手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();

        //2、如过手机号和密码，直接返回登录失败
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "手机号和密码为空");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(queryWrapper);
        if (mobileMember == null) {
            throw new GuliException(20001, "手机号不存在");
        }

        //判断密码是否相等
        //数据库密码进行加密，不能直接对比
        //MD5对密码进行加密，再与数据库进行比较
        String password1 = mobileMember.getPassword();
        if (!MD5.encrypt(password).equals(password1)) {
            throw new GuliException(20001, "密码错误");
        }

        //判断用户是否被禁用
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "用户被禁用登录失败");
        }

        //登录成功
        //按照jwt生产token返回
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return token;
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册基本信息
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //判断是否为空
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "注册失败");
        }
        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        System.out.println("redisCode:" + redisCode);
        System.out.println("code:" + code);
        if (!code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败");
        }

        //判断手机号在数据库中是否存在
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new GuliException(20001, "注册失败");
        }

        //将数据添加到数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setNickname(nickname);
        ucenterMember.setIsDisabled(false);//用户不禁用
        ucenterMember.setAvatar("https://online-education-unicorn.oss-cn-guangzhou.aliyuncs.com/2022/07/27/d016f0cf4dcb45c08afaae087e63f965backiee-204225.jpg");
        baseMapper.insert(ucenterMember);
    }


    @Override
    public Integer ucenterMemberService(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
