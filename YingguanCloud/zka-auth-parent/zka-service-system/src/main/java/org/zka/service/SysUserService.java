package org.zka.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.entity.SysUser;
import org.zka.model.vo.LoginVo;
import org.zka.model.vo.SysUserQueryVo;

import java.util.Map;

/**
 * 用户表 服务类
 */
public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> pageQuery(Long page, Long limit, SysUserQueryVo sysUserQueryVo);

    boolean updateStatus(Long id, Integer status);

    /**
     * 登录功能方法
     * @param sysUser
     * @return
     */
    SysUser login(LoginVo loginVo);

    /**
     * 询出当前下的用户所有信息 （a.用户基本信息  b. 菜单权限   c. 按钮权限）
     * @param userId
     * @param username
     * @return
     */
    Map<String, Object> getUserInfo(Long userId, String username);

    /**
     * 注册功能方法
     * @param sysUser
     * @return
     */
    boolean register(SysUser sysUser);
}
