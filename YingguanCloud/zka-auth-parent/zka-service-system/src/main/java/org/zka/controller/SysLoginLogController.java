package org.zka.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.zka.common.utils.Result;
import org.zka.model.entity.SysLoginLog;
import org.zka.model.vo.SysLoginLogQueryVo;
import org.zka.service.SysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 */
@Api(tags = "登录日志模块")
@RestController
@RequestMapping("/admin/loginLog")
@AllArgsConstructor
public class SysLoginLogController {
    private SysLoginLogService sysLoginLogService;
    //分页＋时间段+登录用户（username）模糊查询
    @GetMapping("/page/{page}/{limit}")
    @ApiOperation("分页查询")
    public Result page(@PathVariable Long page,
                       @PathVariable Long limit,
                       SysLoginLogQueryVo sysLoginLogQueryVo){
        IPage<SysLoginLog> logPage = new Page<>(page,limit);
        logPage=this.sysLoginLogService.pageQuery(logPage,sysLoginLogQueryVo);
        return Result.ok(logPage);
    }
    //删除日志
    @ApiOperation("删除日志")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = this.sysLoginLogService.removeById(id);
        return b?Result.ok():Result.fail();
    }
}
