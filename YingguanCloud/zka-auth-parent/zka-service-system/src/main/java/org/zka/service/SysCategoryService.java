package org.zka.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zka.model.entity.SysCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.vo.SysCategoryQueryVo;

/**
 *  服务类
 */
public interface SysCategoryService extends IService<SysCategory> {

    IPage<SysCategory> pageQuery(Long page, Long limit, SysCategoryQueryVo sysCategoryQueryVo);

}
