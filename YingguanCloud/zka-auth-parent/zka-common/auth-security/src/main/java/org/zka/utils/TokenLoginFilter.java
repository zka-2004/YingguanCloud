package org.zka.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zka.common.utils.IpUtil;
import org.zka.common.utils.JwtHelper;
import org.zka.common.utils.Result;
import org.zka.common.utils.ResultCodeEnum;
import org.zka.model.vo.LoginVo;
import org.zka.service.SysLoginService;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;

    private SysLoginService sysLoginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager,RedisTemplate redisTemplate,SysLoginService sysLoginLogService) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/user/login","POST"));
        this.redisTemplate=redisTemplate;
        this.sysLoginLogService=sysLoginLogService;
    }



    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //1.接收前端表单中传递的 账号 和 密码
        // 利用Jackjson 最原始的代码 去  获取 用户的参数
        ObjectMapper objectMapper = new ObjectMapper();
        LoginVo loginVo = objectMapper.readValue(request.getInputStream(), LoginVo.class);
        //2.将前端传递的账号和密码包装成 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginVo.getUsername(), loginVo.getPassword());
        //3.进行认证 .........authenticate(authRequest)
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    // 认证成功
    // 一但认证成功 需要创建 Jwt 的token令牌
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        HashMap<String, Object> map = new HashMap<>();
        // 1. 获取认证通过的对象
        CustomerUser customerUser = (CustomerUser) authResult.getPrincipal();
        // 2. 从认证通过的对象中获取用户的id 和 用户的 username
        //
        // 两种取出 authorities 权限集合 的方法
        //  authResult.getAuthorities();
        //  Collection<GrantedAuthority> authorities = customerUser.getAuthorities();
        // 当前权限中 redis 的key 是 登录的用户名
        // value  就是权限标识符 将权限存入redis缓存中
        this.redisTemplate.opsForValue().set(customerUser.getSysUser().getUsername(), JSON.toJSONString(customerUser.getAuthorities()));

        //记录登录成功日志
        this.sysLoginLogService.recordLoginLog(customerUser.getSysUser().getUsername(),1, IpUtil.getIpAddress(request),"登录成功");

        //3. 颁发token 令牌
        String token = JwtHelper.createToken(customerUser.getSysUser().getId() + "", customerUser.getSysUser().getUsername());
        //4. 将token 存入到map中
        map.put("token", token);
        //5. 将map转为json格式返回给前端
        ResponseUtil.out(response,Result.ok(map));
    }

    // 认证失败
    // 认证失败 你要给出提示 （账号或密码不正确    其它抛出异常 .....）
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 判断当前 异常类型是否是运行时异常
        if(failed.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.build(null, 204, failed.getMessage()));
        }
        // 不是运行时异常而是认证异常  账号错误  密码错误
        else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PASSWORD_ERROR));
        }
    }
}
