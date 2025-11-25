package org.zka.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    //加密的方法
    @Override
    public String encode(CharSequence password) {
        //将传入的明文密码 进行Md5加密
        String md5Password = DigestUtils.md5DigestAsHex(password.toString().getBytes());
        return md5Password;
    }

    //比较的方法
    @Override
    public boolean matches(CharSequence password, String passwordDB) {
        String encodedRawPassword = this.encode(password); // 明文先加密
        return encodedRawPassword.equals(passwordDB);    // 与数据库密文比对
    }
}
