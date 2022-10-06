package com.atguigu.serurity.security;

import com.example.commonutils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <p>
 * t密码的处理方法类型
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength
     *            the log rounds to use, between 4 and 31
     */
    public DefaultPasswordEncoder(int strength) {

    }

    //加密
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    //验证密码
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}