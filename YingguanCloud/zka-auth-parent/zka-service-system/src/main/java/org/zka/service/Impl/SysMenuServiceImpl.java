package org.zka.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.zka.mapper.SysRoleMenuMapper;
import org.zka.model.entity.SysMenu;
import org.zka.mapper.SysMenuMapper;
import org.zka.model.entity.SysRoleMenu;
import org.zka.model.vo.AssignMenuVo;
import org.zka.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zka.system.exception.MyException;
import org.zka.utils.MenuHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zka
 * @since 2025-10-13
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    //菜单表 Mapper
    private SysMenuMapper sysMenuMapper;
    // 角色菜单表 Mapper
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    public List<SysMenu> findAll() {
        //1.获取所有的菜单
        List<SysMenu> sysMenus = this.sysMenuMapper.selectList(null);
        //2.所有菜单数据转换要求数据格式
        List<SysMenu> bulidTree = MenuHelper.bulidTree(sysMenus);
        return bulidTree;
    }

    @Override
    public boolean removeMenuById(Long id) {
        //1.根据parentId 查询是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);
        int count = this.sysMenuMapper.selectCount(wrapper);
        //2.如果存在子菜单，则不允许删除，抛出自定义异常
        if(count>0){
            //存在子菜单
            throw new MyException(201,"存在子菜单，不允许删除，请先删除子菜单");
        }
        int result = this.sysMenuMapper.deleteById(id);
        if(result>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus,1);
        //1.获取所有status为1的权限列表
        List<SysMenu> sysMenus = this.sysMenuMapper.selectList(wrapper);
        //2.构造条件
        LambdaQueryWrapper<SysRoleMenu> roleMenuQw = new LambdaQueryWrapper<>();
        roleMenuQw.eq(SysRoleMenu::getRoleId,roleId);
        //2.根据角色id获取角色权限
        List<SysRoleMenu> sysRoleMenus = this.sysRoleMenuMapper.selectList(roleMenuQw);
        //3.取该角色已分配的所有菜单权限id
        List<Long> roleMenuIds = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
            if (sysRoleMenu!=null){
                roleMenuIds.add(sysRoleMenu.getMenuId());
            }
        }
        //4.遍历所有启用菜单列表，设置是否选中
        for (SysMenu sysMenu : sysMenus) {
            if (roleMenuIds.contains(sysMenu.getId())){
                //已分配，说明设置选中
                sysMenu.setSelect(true);
            }else {
                sysMenu.setSelect(false);
            }
        }
        //5.封装菜单权限树
        List<SysMenu> sysMenus1 = MenuHelper.bulidTree(sysMenus);
        return sysMenus1;
    }

    @Override
    public void toAssign(AssignMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> roleMenuQW = new LambdaQueryWrapper<>();
        roleMenuQW.eq(SysRoleMenu::getRoleId,assignMenuVo.getRoleId());
        //1.根据角色ID删除该角色所有已分配菜单权限
        this.sysRoleMenuMapper.delete(roleMenuQW);
        //2.遍历所有已选择的菜单（权限）id
        for (Long menuId : assignMenuVo.getMenuIdList()) {
            if (menuId!=null){
                //创建SysRoleMenu对象
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
                //添加新权限
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }

    @Override
    public List<SysMenu> findMenuListByUserId(Long userId) {
        // 默认 admin 是超级管理员 拥有最高权 限查询全部
        List<SysMenu> menus=null;
        if (userId==1){
            // 管理员
            menus = this.sysMenuMapper.selectList(null);
        }else {
            //就是其他用户
            menus=this.sysMenuMapper.selectMenusByUserId(userId);
        }
        return menus;
    }

    @Override
    public List<String> findButtonsByUserId(Long userId) {
        // 默认 admin 是超级管理员 拥有最高权 限查询全部
        List<String> buttons = new ArrayList<>();
        if (userId==1){
            // 管理员
            List<SysMenu> sysMenus = this.sysMenuMapper.selectList(null);
            for (SysMenu sysMenu : sysMenus) {
                if (sysMenu.getPerms()!=null&&sysMenu.getType()==2){
                    buttons.add(sysMenu.getPerms());
                }
            }
        }else {
            //就是其他用户
            List<SysMenu> sysMenus = this.sysMenuMapper.selectMenusByUserId(userId);
            for (SysMenu sysMenu : sysMenus) {
                if (sysMenu.getPerms()!=null&&sysMenu.getType()==2){
                    buttons.add(sysMenu.getPerms());
                }
            }
        }
        return buttons;
    }
}
