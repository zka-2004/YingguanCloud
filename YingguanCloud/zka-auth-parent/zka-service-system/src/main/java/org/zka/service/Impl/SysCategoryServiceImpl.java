package org.zka.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.model.entity.SysCategory;
import org.zka.mapper.SysCategoryMapper;
import org.zka.model.vo.SysCategoryQueryVo;
import org.zka.service.SysCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zka
 * @since 2025-10-18
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements SysCategoryService {

    @Override
    public IPage<SysCategory> pageQuery(Long page, Long limit, SysCategoryQueryVo sysCategoryQueryVo) {
        IPage<SysCategory> pageModel = new Page<>(page,limit);
        LambdaQueryWrapper<SysCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(sysCategoryQueryVo.getName() != null,SysCategory::getName,sysCategoryQueryVo.getName())
                .orderByDesc(SysCategory::getId);
        return baseMapper.selectPage(pageModel,wrapper);
    }
}
