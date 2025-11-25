package org.zka.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.common.utils.Result;
import org.zka.model.entity.SysRole;
import org.zka.model.vo.AssignRoleVo;
import org.zka.model.vo.SysRoleQueryVo;
import org.zka.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理控制器")
@RestController
@RequestMapping("/admin/Role")
@AllArgsConstructor
public class SysRoleController {
    private SysRoleService sysRoleService;

    //查询所有角色信息
/*    @ApiOperation("查询所有角色信息")
    @GetMapping("/list")
    public List<SysRole> findAll(){
        try {
            int i=1/0;
        } catch (Exception e) {
            throw new MyException(1101,"自定义异常！！");
        }
        return this.sysRoleService.list();
    }*/

    //  根据当前页码+ 每页条数 + 搜索条件   去实现查询
    @ApiOperation("分页和模糊查询")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("page/{page}/{limit}")
    public Result pageQuery(@PathVariable Long page,
                            @PathVariable Long limit,
                            SysRoleQueryVo sysRoleQueryVo){
        IPage<SysRole> sysRolePage = new Page<>();
        sysRolePage=this.sysRoleService.pageQuery(page,limit,sysRoleQueryVo);
        return Result.ok(sysRolePage);
    }

    //新增角色
    @ApiOperation("新增角色")
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PostMapping("/add")
    public Result addRole(@RequestBody SysRole sysRole){
        boolean b = this.sysRoleService.save(sysRole);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据ID查询角色回显
    @ApiOperation("ID查询角色")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/getRoleById/{id}")
    public Result getRoleById(@PathVariable Long id){
        SysRole role = this.sysRoleService.getById(id);
        return Result.ok(role);
    }

    //修改角色
    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PostMapping("/updateRole")
    public Result updateRoel(@RequestBody SysRole sysRole){
        boolean b = this.sysRoleService.updateById(sysRole);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //根据Id逻辑删除角色
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据Id逻辑删除角色")
    public Result removeById(@PathVariable Long id){
        boolean b = this.sysRoleService.removeById(id);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据ID批量删除角色
    @ApiOperation("根据ID批量删除角色")
    @DeleteMapping("/batchRemove")
    public Result batchRemoveRolesByIds(@RequestBody List<Long> ids){
        boolean b = this.sysRoleService.removeByIds(ids);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据用户id查询用户已分配的角色
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String,Object> roleMap = this.sysRoleService.getRolesByUserId(userId);
        return Result.ok(roleMap);
    }

    //给用户分配角色 思路：先删除原来分配的全部旧角色，再进行添加新分配的角色
    @ApiOperation("给用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo){
        try {
            this.sysRoleService.doAssign(assignRoleVo);
            return Result.ok();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
