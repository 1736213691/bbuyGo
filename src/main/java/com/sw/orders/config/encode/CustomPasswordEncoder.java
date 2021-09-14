package com.sw.orders.config.encode;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * spring security的密码编码解码器
 * 自定义使用md5加密
 */
public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
//        return true;
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()).equals(s);
    }
}