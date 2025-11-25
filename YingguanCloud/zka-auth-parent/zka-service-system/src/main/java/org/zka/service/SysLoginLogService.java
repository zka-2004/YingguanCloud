package org.zka.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zka.model.entity.SysLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.vo.SysLoginLogQueryVo;

/**
 * 系统访问记录 服务类
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    IPage<SysLoginLog> pageQuery(IPage<SysLoginLog> logPage, SysLoginLogQueryVo sysLoginLogQueryVo);
}
