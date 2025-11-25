package org.zka.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.zka.model.entity.SysUser;
import org.zka.service.SysMenuService;
import org.zka.service.SysUserService;
import org.zka.utils.CustomerUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private SysUserService sysUserService;
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据用户名去查询当前用户信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser user = this.sysUserService.getOne(wrapper);
        // 2. 判断用户是否被查询到
        if (user==null){
            throw new RuntimeException("该用户不存在，UserDetailServiceImpl");
        }
        // 3. 密码判断  这里暂时就不需要做了，因为在 CustomMd5PasswordEncoder 已经做了
        // 4.判断用户状态
        if (user.getStatus()==0){
            throw new RuntimeException("该用户已被禁用");
        }
        // 5. 用户查询成功
        // UserDetails  接口 -》唯一实现类 User ities
        //public User(String username, String password, Collection<? extends GrantedAuthority > authorities)
        //  6. 认证通过后，就需要授权  (这里先要根据不同用户查询出不同的按钮权限标识)
        List<String> buttons = this.sysMenuService.findButtonsByUserId(user.getId());
        //
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String button : buttons) {
            // 重新放入到 List<SimpleGrantedAuthority> 集合中
            authorities.add(new SimpleGrantedAuthority(button));
        }
        return new CustomerUser(user, authorities);
    }
}
