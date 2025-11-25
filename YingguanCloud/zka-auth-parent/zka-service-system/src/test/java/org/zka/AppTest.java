package org.zka;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.mapper.SysRoleMapper;
import org.zka.model.entity.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple ServiceApp.
 */
@SpringBootTest(classes = ServiceApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void test1(){
        Page<SysRole> sysRolePage = new Page<>(1, 4);
        sysRoleMapper.selectPage(sysRolePage,null);
//        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
//        if (!sysRoles.isEmpty()) {
//            sysRoles.forEach(System.out::println);
//        }
    }

    @Test
    //测试字段自动填充是否有效
    public void test2(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("王林");
        sysRole.setRoleCode("wanglin");
        sysRole.setDescription("测试账号");
        this.sysRoleMapper.insert(sysRole);
    }


}
