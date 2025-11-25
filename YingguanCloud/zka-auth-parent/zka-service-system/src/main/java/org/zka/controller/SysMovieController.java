package org.zka.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.common.utils.Result;
import org.zka.common.utils.VodTemplate;
import org.zka.model.entity.SysMovie;
import org.zka.model.vo.SysMovieQueryVo;
import org.zka.model.vo.SysMovieVo;
import org.zka.service.SysMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 *  前端控制器
 */
@RestController
@Api(tags = "影视模块开发")
@RequestMapping("/admin/movie")
@AllArgsConstructor
public class SysMovieController {

    private SysMovieService sysMovieService;
    private VodTemplate vodTemplate;

    @GetMapping("/page/{page}/{limit}")
    @ApiOperation("分页查询")
    //分页查询＋影视名模糊查询
    public Result page(@PathVariable Long page,
                       @PathVariable Long limit,
                       SysMovieQueryVo sysMovieQueryVo
                       ){
        IPage<SysMovieVo> sysMoviePage = new Page<>();
//        sysMoviePage=this.sysMovieService.pageQuery(page,limit,sysMovieQueryVo);
        sysMoviePage=this.sysMovieService.pageQuery(page,limit,sysMovieQueryVo);
        return Result.ok(sysMoviePage);
    }

    //修改影视
    @ApiOperation("修改影视")
    @PostMapping("/updateMovie")
    public Result updateMovie(@RequestBody SysMovie sysMovie){
        boolean b = sysMovieService.updateById(sysMovie);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //添加影视
    @ApiOperation("添加影视")
    @PostMapping("/addMovie")
    public Result addMovie(@RequestBody SysMovie sysMovie){
        boolean b = sysMovieService.save(sysMovie);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //根据ID查询影视
    @ApiOperation("根据ID查询影视")
    @GetMapping("/getMovieById/{id}")
    public Result getMovieById(@PathVariable Long id){
        SysMovie movie = sysMovieService.getById(id);
        return Result.ok(movie);
    }

    //删除影视
    @ApiOperation("删除影视")
    @DeleteMapping("/deleteMovieById/{id}")
    public Result deleteMovieById(@PathVariable Long id){
        boolean b = sysMovieService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    //根据id获取palyAuth播放凭证
    @ApiOperation("根据id获取palyAuth播放凭证")
    @GetMapping("/getPlayAuthById/{id}")
    public Result getPlayAuthById(@PathVariable Long id) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        // 1.根据id查询出视频对象
        SysMovie sysMovie = this.sysMovieService.getById(id);
        // 2.调用VodTemplate获取播放凭证
        String videoPlayAuth = this.vodTemplate.getVideoPlayAuth(sysMovie.getPlayId()).getPlayAuth();
        map.put("playId",sysMovie.getPlayId());
        map.put("playAuth",videoPlayAuth);
        return Result.ok(map);
    }
}
