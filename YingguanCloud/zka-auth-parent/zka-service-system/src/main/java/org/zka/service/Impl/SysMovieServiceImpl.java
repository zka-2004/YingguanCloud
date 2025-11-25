package org.zka.service.Impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.mapper.SysMovieMapper;
import org.zka.model.entity.SysMovie;
import org.zka.model.vo.SysMovieQueryVo;
import org.zka.model.vo.SysMovieVo;
import org.zka.service.SysMovieService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zka
 * @since 2025-10-17
 */
@Service
@AllArgsConstructor
public class SysMovieServiceImpl extends ServiceImpl<SysMovieMapper, SysMovie> implements SysMovieService {

    private SysMovieMapper sysMovieMapper;
    @Override
    public IPage<SysMovieVo> pageQuery(Long page, Long limit, SysMovieQueryVo sysMovieQueryVo) {
//        IPage<SysMovie> moviePage = new Page<>(page,limit);
//        boolean isVo=false;
//        if (sysMovieQueryVo!=null){
//            isVo=sysMovieQueryVo.getName()!=null;
//        }
//        LambdaQueryWrapper<SysMovie> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(isVo,SysMovie::getName,sysMovieQueryVo.getName())
//                .orderByDesc(SysMovie::getId);
//        IPage<SysMovie> sysMovieIPage = this.sysMovieMapper.selectPage(moviePage, wrapper);
        Page<SysMovieVo> sysMoviePage = new Page<>(page,limit);
        sysMoviePage=this.sysMovieMapper.selectPage(sysMoviePage,sysMovieQueryVo);
//        List<SysMovieVo> records = sysMoviePage.getRecords();
//        System.out.println("-------------------------------------");
//        records.forEach(System.out::println);
        return sysMoviePage;
    }

}
