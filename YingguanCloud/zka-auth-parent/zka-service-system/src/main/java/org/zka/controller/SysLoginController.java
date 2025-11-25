package org.zka.controller;

import org.zka.common.utils.JwtHelper;
import org.zka.common.utils.Result;
import org.zka.model.entity.SysUser;
import org.zka.model.vo.LoginVo;
import org.zka.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 */
@Api(tags = "登录控制器")
@AllArgsConstructor
@RestController
@RequestMapping("/admin/user")
public class SysLoginController {

    private SysUserService sysUserService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){

        SysUser sysUserLogin= this.sysUserService.login(loginVo);

        String token = JwtHelper.createToken(String.valueOf(sysUserLogin.getId()), sysUserLogin.getUsername());

        Map<String, Object> map = new HashMap<>();
        //##TODO 状态码 前端需要的是 2000，我们返回的是 200，将来在前端解决!
//        String password = DigestUtils.md5DigestAsHex();
        map.put("token",token);
        return Result.ok(map);
    }

    //显示欢迎
    @ApiOperation("登录后获取用户信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        // 1. 从axios 前端中接收 token 值
        String token = request.getHeader("token");
        //2. 获取token中id 或者 username
        Long userId = Long.valueOf(JwtHelper.getUserId(token));
        String username = JwtHelper.getUsername(token);
        //3.  根据当前用户name 去查询出当前下的 所有信息 （a.用户基本信息  b. 菜单权限   c. 按钮权限）
        map=this.sysUserService.getUserInfo(userId,username);
        return Result.ok(map);
    }


    @ApiOperation("注册账号")
    @PostMapping("/register")
    public Result register(@RequestBody SysUser sysUser){
        boolean b = sysUserService.register(sysUser);
        return b?Result.ok():Result.fail();
    }
}
