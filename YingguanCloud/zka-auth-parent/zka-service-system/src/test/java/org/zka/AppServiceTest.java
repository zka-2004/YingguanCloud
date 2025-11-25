package org.zka;

import org.zka.model.entity.SysRole;
import org.zka.model.vo.SysRoleQueryVo;
import org.zka.service.SysRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes = ServiceApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AppServiceTest {
    @Autowired
    private SysRoleService sysRoleService;

    @Test
    //测试service是否可用
    public void test1(){
        List<SysRole> list = this.sysRoleService.list();
    }

    //分页测试
    @Test
    public void test3(){
        SysRoleQueryVo sysRoleQueryVo = new SysRoleQueryVo();
        sysRoleQueryVo.setRoleName("飞");
        this.sysRoleService.pageQuery(1L,4L,sysRoleQueryVo);
    }
}
