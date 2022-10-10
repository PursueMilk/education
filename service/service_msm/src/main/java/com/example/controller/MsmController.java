package com.example.controller;

import com.example.commonutils.R;
import com.example.commonutils.RandomUtil;
import com.example.service.MsmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    //redis设置过期时间
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //1、从redis中获取验证码，如果获取到直接返回
        log.info("phone:{}", phone);
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        //2、如果redis获取不到，进行阿里云发送
        //生成随机数，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        //调用service的方法，发送
        boolean isSend = msmService.send(code,phone);
        if (isSend) {
            //阿里云发送成功，把发送成功的验证码放入redis缓存中
            //设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else
            return R.error().message("短信发送失败");
    }

    //发送邮件
    @GetMapping("sendMail/{mail}")
    public R sendMail(@PathVariable String mail) {
        //1、从redis中获取验证码，如果获取到直接返回
        log.info("mail:{}", mail);
        String code = redisTemplate.opsForValue().get(mail);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        //2、如果redis获取不到，进行邮件发送
        code = RandomUtil.getFourBitRandom();
        boolean isSend = msmService.sendMail(code,mail);
        if (isSend) {
            //阿里云发送成功，把发送成功的验证码放入redis缓存中
            //设置有效时间
            redisTemplate.opsForValue().set(mail,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else
            return R.error().message("邮件发送失败");
    }
}