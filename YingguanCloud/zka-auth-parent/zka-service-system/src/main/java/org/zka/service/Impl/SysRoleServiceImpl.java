package org.zka.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zka.mapper.SysRoleMapper;
import org.zka.mapper.SysUserRoleMapper;
import org.zka.model.entity.SysRole;
import org.zka.model.entity.SysUserRole;
import org.zka.model.vo.AssignRoleVo;
import org.zka.model.vo.SysRoleQueryVo;
import org.zka.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    // 角色表 mapper
    private SysRoleMapper sysRoleMapper;
    // 用户角色表 mapper
    private SysUserRoleMapper sysUserRoleMapper;
    @Override
    public IPage<SysRole> pageQuery(Long pageIndex, Long pageSize, SysRoleQueryVo sysRoleQueryVo) {
        boolean isVo=false;
        if (sysRoleQueryVo != null) {
            isVo=sysRoleQueryVo.getRoleName()!=null;
        }
        Page<SysRole> sysRolePage = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<SysRole> sysRoleWrapper = new LambdaQueryWrapper<>();
        sysRoleWrapper.like(isVo,SysRole::getRoleName,sysRoleQueryVo.getRoleName())
                .orderByDesc(SysRole::getId);
        return this.sysRoleMapper.selectPage(sysRolePage, sysRoleWrapper);
    }

    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        //1.获取所有角色
        List<SysRole> sysRoles = this.sysRoleMapper.selectList(null);
        //2.根据用户id查询分配的角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        //构造条件
        wrapper.eq(userId!=null,SysUserRole::getUserId,userId);
        //3.获取用户已分配的角色
        List<SysUserRole> sysUserRoles = this.sysUserRoleMapper.selectList(wrapper);
        //4.获取用户已分配的角色id
        ArrayList<Long> userRoleIds = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoles) {
            userRoleIds.add(sysUserRole.getRoleId());
        }
        //5.返回结果
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",sysRoles);
        returnMap.put("userRoleIds",userRoleIds);
        return returnMap;
    }

    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
        //1.删除原来分配的角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assignRoleVo.getUserId());
        this.sysUserRoleMapper.delete(wrapper);
        //3.获取所有的角色id
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if (roleId!=null){
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assignRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);
                //添加新分配的角色
                this.sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }
}
