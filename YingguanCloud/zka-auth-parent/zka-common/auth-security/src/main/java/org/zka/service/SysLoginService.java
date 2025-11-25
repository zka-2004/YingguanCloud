package org.zka.service;

public interface SysLoginService {
    void recordLoginLog(String username,Integer status, String ipaddr,  String msg);
}
