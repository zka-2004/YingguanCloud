package org.zka.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zka.model.entity.SysMovie;
import org.zka.model.vo.SysMovieQueryVo;
import org.zka.model.vo.SysMovieVo;

/**
 *  服务类
 */

public interface SysMovieService extends IService<SysMovie> {

    IPage<SysMovieVo> pageQuery(Long page, Long limit, SysMovieQueryVo sysMovieQueryVo);

}
