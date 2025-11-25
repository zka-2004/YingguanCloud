package org.zka;

import org.zka.common.utils.JwtHelper;
import org.junit.Test;

public class TestJwt {
    private String token;
    @Test
    //测试创建jwt令牌
    public void test1(){
        token = JwtHelper.createToken("11", "wangLin");
        System.out.println(token);
    }

    @Test
    //获取userId （校验token）
    public void test2(){
        String userId = JwtHelper.getUserId(token);
        System.out.println("id："+userId);
    }
}
