package org.zka.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zka.common.utils.Result;
import org.zka.model.entity.SysCategory;
import org.zka.model.vo.SysCategoryQueryVo;
import org.zka.service.SysCategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *  前端控制器
 */
@RestController
@RequestMapping("/admin/category")
@AllArgsConstructor
public class SysCategoryController {
    private SysCategoryService sysCategoryService;

    // 分页＋栏目名（name）模糊查询
    @GetMapping("/page/{page}/{limit}")
    @ApiOperation("分页查询")
    public Result page(@PathVariable Long page,
                       @PathVariable Long limit,
                       SysCategoryQueryVo sysCategoryQueryVo){
        IPage<SysCategory> sysCategoryPage= this.sysCategoryService.pageQuery(page,limit,sysCategoryQueryVo);
        return Result.ok(sysCategoryPage);
    }
    // 添加分类栏目
    @ApiOperation("添加栏目")
    @PostMapping("/add")
    public Result add(@RequestBody SysCategory sysCategory){
        boolean b = sysCategoryService.save(sysCategory);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    // 修改分类栏目
    @ApiOperation("修改栏目")
    @PostMapping("/update")
    public Result update(@RequestBody SysCategory sysCategory){
        boolean b = sysCategoryService.updateById(sysCategory);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    // 根据ID查询分类栏目
    @ApiOperation("根据ID查询栏目")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        SysCategory sysCategory = sysCategoryService.getById(id);
        return Result.ok(sysCategory);
    }
    // 根据ID删除分类栏目
    @ApiOperation("根据ID删除栏目")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = sysCategoryService.removeById(id);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //查询所有栏目
    @ApiOperation("查询所有栏目")
    @GetMapping("/list")
    public Result list(){
        return Result.ok(sysCategoryService.list());
    }
}
