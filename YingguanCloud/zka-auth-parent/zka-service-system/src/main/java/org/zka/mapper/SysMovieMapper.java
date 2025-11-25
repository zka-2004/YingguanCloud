package org.zka.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.model.entity.SysMovie;
import org.zka.model.vo.SysMovieQueryVo;
import org.zka.model.vo.SysMovieVo;
import org.springframework.data.repository.query.Param;

/**
 *  Mapper 接口
 */

public interface SysMovieMapper extends BaseMapper<SysMovie> {

    Page<SysMovieVo> selectPage(IPage<SysMovieVo> page,@Param("vo") SysMovieQueryVo vo);
}
