package org.zka.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.entity.SysRole;
import org.zka.model.vo.AssignRoleVo;
import org.zka.model.vo.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> pageQuery(Long pageIndex, Long pageSize, SysRoleQueryVo sysRoleQueryVo);

    Map<String, Object> getRolesByUserId(Long userId);

    void doAssign(AssignRoleVo assignRoleVo);

}
