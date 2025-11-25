package org.zka.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.zka.common.utils.RouterHelper;
import org.zka.mapper.SysUserMapper;
import org.zka.model.entity.SysMenu;
import org.zka.model.entity.SysUser;
import org.zka.model.vo.LoginVo;
import org.zka.model.vo.RouterVo;
import org.zka.model.vo.SysUserQueryVo;
import org.zka.service.SysMenuService;
import org.zka.service.SysUserService;
import org.zka.system.exception.MyException;
import org.zka.utils.MenuHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 86151
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-10-11 10:20:07
*/
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

    private SysUserMapper sysUserMapper;

    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> pageQuery(Long page, Long limit, SysUserQueryVo sysUserQueryVo) {
        boolean isKeyword;
        boolean isBeginTime=false;
        boolean isEndTime=false;
        if (sysUserQueryVo!=null){
            // 优化 isKeyword 的判定：仅当 keyword 不为 null 且不是空字符串（含"null"）时才为 true
            isKeyword = sysUserQueryVo.getKeyword() != null
                    && !sysUserQueryVo.getKeyword().trim().isEmpty()
                    && !"null".equals(sysUserQueryVo.getKeyword().trim());
            isBeginTime=sysUserQueryVo.getCreateTimeBegin()!=null;
            isEndTime=sysUserQueryVo.getCreateTimeEnd()!=null;
        } else {
            isKeyword = false;
        }
        Page<SysUser> userpage = new Page<>(page,limit);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 1.nested 包裹关键词的 OR 条件组 可根据用户名/姓名/手机号码 模糊查询
        if (isKeyword){
            wrapper.nested(i->i.like(isKeyword,SysUser::getUsername,sysUserQueryVo.getKeyword())
                    .or(isKeyword).like(isKeyword,SysUser::getName,sysUserQueryVo.getKeyword())
                    .or(isKeyword).like(isKeyword,SysUser::getPhone,sysUserQueryVo.getKeyword()));
        }
        //2. 追加时间条件（与关键词组为 AND 关系） 根据时间段查询
        wrapper.ge(isBeginTime,SysUser::getCreateTime,sysUserQueryVo.getCreateTimeBegin())
                .le(isEndTime,SysUser::getCreateTime,sysUserQueryVo.getCreateTimeEnd())
                .orderByDesc(SysUser::getUpdateTime);
        return sysUserMapper.selectPage(userpage, wrapper);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SysUser user = this.sysUserMapper.selectById(id);
        user.setStatus(status);
        int i = this.sysUserMapper.updateById(user);
        if (i>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public SysUser login(LoginVo loginVo) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,loginVo.getUsername());
        //1.将传入的用户名进行查询
        SysUser user = this.sysUserMapper.selectOne(wrapper);
        if (user!=null){
            //2.将 明文密码 转换为 密文
            String loginPsw = DigestUtils.md5DigestAsHex(loginVo.getPassword().getBytes());
            if (!user.getPassword().equals(loginPsw)){
                throw new MyException(20001,"密码错误！");
            }
            //检测该用户状态是否正常
            if (user.getStatus()!=1){
                throw new MyException(20001,"该用户已被禁用，请联系管理员");
            }
            return user;
        }else {
            throw new MyException(20001,"用户名不存在！");
        }
    }

    @Override
    public Map<String, Object> getUserInfo(Long userId, String username) {
        HashMap<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        //1.查询基本信息
        SysUser user = this.sysUserMapper.selectOne(wrapper);
        //2.封装基本信息（头像和name）
        map.put("avatar",user.getHeadUrl());
        map.put("name",user.getName());
        //3.查询菜单权限
        //因为前端需要的树结构数据，我们这里需要将List集合转为 树
        List<SysMenu> menuList= this.sysMenuService.findMenuListByUserId(userId);
        // a. 将菜单集合转为 树
        List<SysMenu> sysMenus = MenuHelper.bulidTree(menuList);
        // b. 第二次转换 是需要将树转为 Vue-router需要的树
        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenus);
        //4. 查询按钮权限
        List<String> buttons= this.sysMenuService.findButtonsByUserId(userId);
        //封装到集合
        map.put("routers", routerVos);
        map.put("buttons", buttons);
        return map;
    }

    /**
     * 注册功能
     * @param sysUser 用户信息
     * @return 注册结果
     */
    @Override
    public boolean register(SysUser sysUser) {
        return this.save(sysUser);
    }
}




