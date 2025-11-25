package org.zka.utils;

import com.alibaba.fastjson.JSON;
import org.zka.common.utils.JwtHelper;
import org.zka.common.utils.Result;
import org.zka.common.utils.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 认证解析过滤器
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter() {

    }
    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:"+request.getRequestURI());

        //如果是登录接口，直接放行
        if("/admin/user/login".equals(request.getRequestURI())
                || "/admin/system/upload/uploadImage".equals(request.getRequestURI())
                || "/admin/system/upload/uploadVideo".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        // 调用了下面的方法
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 1.从请求中 header 中 获取token
        String token = request.getHeader("token");
        logger.info("token:"+token);
        // 2.判断token是否为null
        if (!StringUtils.isEmpty(token)) {
            //3.当token不为null，就从token 中解析获取 到username
            String username = JwtHelper.getUsername(token);
            logger.info("useruame:"+username);
            //4.如果 username 不为null
            if (!StringUtils.isEmpty(username)) {
                // 从redis中根据 username 中这个key 取出缓存的 权限标识
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                // 将缓存的 json字符串 权限标识 还原为 List 集合
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                }
                // 5.就认证成功
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        }
        return null;
    }
}
