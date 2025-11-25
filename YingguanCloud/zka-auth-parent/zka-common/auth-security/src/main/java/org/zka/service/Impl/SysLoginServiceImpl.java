package org.zka.service.Impl;

import org.zka.mapper.SysLoginMapper;
import org.zka.model.entity.SysLoginLog;
import org.zka.service.SysLoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {
    private SysLoginMapper sysLoginMapper;
    @Override
    public void recordLoginLog(String username, Integer status, String ipaddr, String msg) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setStatus(status);
        sysLoginLog.setIpaddr(ipaddr);
        sysLoginLog.setMsg(msg);
        this.sysLoginMapper.insert(sysLoginLog);
        System.out.println("记录登录日志成功");
    }
}
