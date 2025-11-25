package org.zka.service;

import org.zka.model.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.vo.AssignMenuVo;

import java.util.List;

/**
 * 菜单表 服务类
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findAll();

    boolean removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);

    void toAssign(AssignMenuVo assignMenuVo);
    /**
     *  根据用户id去查询当前下的菜单权限
     * @param id
     * @return
     */
    List<SysMenu> findMenuListByUserId(Long userId);
    /**
     *  根据当前用户id去查询当前用户下的按钮权限
     * @param id
     * @return
     */
    List<String> findButtonsByUserId(Long userId);
}
