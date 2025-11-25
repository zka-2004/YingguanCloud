package org.zka.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zka.model.entity.SysLoginLog;
import org.zka.mapper.SysLoginLogMapper;
import org.zka.model.vo.SysLoginLogQueryVo;
import org.zka.service.SysLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author zka
 * @since 2025-10-20
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
   
    @Override
    public IPage<SysLoginLog> pageQuery(IPage<SysLoginLog> logPage, SysLoginLogQueryVo queryVo) {
        boolean username;
        boolean isBeginTime=false;
        boolean isEndTime=false;
        if (queryVo!=null){
            // 优化 username 的判定：仅当 username 不为 null 且不是空字符串（含"null"）时才为 true
            username = queryVo.getUsername() != null
                    && !queryVo.getUsername().trim().isEmpty()
                    && !"null".equals(queryVo.getUsername().trim());
            isBeginTime=queryVo.getCreateTimeBegin()!=null;
            isEndTime=queryVo.getCreateTimeEnd()!=null;
        } else {
            username = false;
        }
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        if (username){
            wrapper.like(SysLoginLog::getUsername,queryVo.getUsername());
        }
        wrapper.ge(isBeginTime,SysLoginLog::getCreateTime,queryVo.getCreateTimeBegin())
                .le(isEndTime,SysLoginLog::getCreateTime,queryVo.getCreateTimeEnd())
                .orderByDesc(SysLoginLog::getCreateTime);
        return this.page(logPage,wrapper);
    }
}
