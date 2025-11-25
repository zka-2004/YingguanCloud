package org.zka.controller;


import org.zka.common.utils.Result;
import org.zka.model.entity.SysMenu;
import org.zka.model.vo.AssignMenuVo;
import org.zka.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/admin/menu")
@Api(tags = "菜单管理控制器")
@AllArgsConstructor
public class SysMenuController {

    private SysMenuService sysMenuService;

    //查询获取所有菜单信息
    @ApiOperation("查询所有菜单信息")
    @GetMapping("/findList")
    public Result findAll(){
        List<SysMenu> sysMenus = this.sysMenuService.findAll();
        return Result.ok(sysMenus);
    }

    //添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("/add")
    public Result addMenu(@RequestBody SysMenu sysMenu){
        boolean b = this.sysMenuService.save(sysMenu);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据ID回显菜单信息
    @ApiOperation("根据ID回显菜单信息")
    @GetMapping("/getMenuById/{id}")
    public Result getMenuById(@PathVariable Long id){
        SysMenu menu = this.sysMenuService.getById(id);
        if(menu!=null){
            return Result.ok(menu);
        }else {
            return Result.fail();
        }
    }

    //根据ID修改菜单
    @ApiOperation("根据ID修改菜单")
    @PostMapping("/update")
    public Result updateMenu(@RequestBody SysMenu sysMenu){
        boolean b = this.sysMenuService.updateById(sysMenu);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据ID删除菜单
    @ApiOperation("根据ID删除菜单")
    @DeleteMapping("/removeMenu/{id}")
    public Result removeMenu(@PathVariable Long id){
        boolean b = this.sysMenuService.removeMenuById(id);
        if(b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //根据ID修改菜单状态
    /*@ApiOperation("根据ID修改菜单状态")
    @PostMapping("/updateStatus/{id}/{status)")
    public Result updateStatus(@PathVariable Long id,
                               @PathVariable Integer status
                               ){
        this.sysMenuService.update
    }*/

    //根据角色ID获取分配菜单
    @ApiOperation("根据角色ID获取分配菜单权限")
    @GetMapping("/toAssign/{id}")
    public Result toAssign(@PathVariable Long id){
        List<SysMenu> sysMenus = this.sysMenuService.findMenuByRoleId(id);
        return Result.ok(sysMenus);
    }
    //给角色分配菜单权限
    @ApiOperation("给角色分配菜单权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo){
        try {
            this.sysMenuService.toAssign(assignMenuVo);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail().message(e.getMessage());
        }
    }
}
