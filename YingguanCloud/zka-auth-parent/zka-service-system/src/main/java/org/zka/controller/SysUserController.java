package org.zka.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.common.utils.Result;
import org.zka.model.entity.SysUser;
import org.zka.model.vo.SysUserQueryVo;
import org.zka.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "用户管理控制器")
@RestController
@RequestMapping("/admin/user")
@AllArgsConstructor
public class SysUserController {

    private SysUserService userService;
    //用户分页＋条件查询
    @ApiOperation("分页查询")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("page/{page}/{limit}")
    public Result pageQuery(@PathVariable Long page,
                            @PathVariable Long limit,
                            SysUserQueryVo sysUserQueryVo){
        IPage<SysUser> sysUserPage = new Page<>();
        sysUserPage=this.userService.pageQuery(page,limit,sysUserQueryVo);
        return Result.ok(sysUserPage);
    }

    //添加用户
    @ApiOperation("添加用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @PostMapping("/add")
    public Result saveUser(@RequestBody SysUser sysUser){
        //调用spring带的 DigestUtils 工具类 进行 MD5加密
        String passwordWithMD5 = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        log.info(passwordWithMD5);
        //将加密的密文密码设置
        sysUser.setPassword(passwordWithMD5);
        boolean b = this.userService.save(sysUser);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //删除逻辑用户
    @ApiOperation("删除用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("remove/{id}")
    public Result removeUser(@PathVariable Long id){
        boolean b = this.userService.removeById(id);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //批量删除用户
    @ApiOperation("批量删除用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("/batchUserById")
    public Result batchUserById(@RequestBody List<Long> ids){
        boolean b = this.userService.removeByIds(ids);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //修改用户
    @ApiOperation("修改用户")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody SysUser sysUser){
//        String passwordWithMd5 = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
//        sysUser.setPassword(passwordWithMd5);
        boolean b = this.userService.updateById(sysUser);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据ID回显
    @ApiOperation("根据ID回显")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/getUserById/{id}")
    public Result getUserById(@PathVariable Long id){
        SysUser user = this.userService.getById(id);
        if (user!=null){
            return Result.ok(user);
        }else {
            return Result.fail();
        }
    }

    //修改用户状态
    @ApiOperation("修改用户状态")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,
                               @PathVariable Integer status){
        boolean b= this.userService.updateStatus(id,status);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}
