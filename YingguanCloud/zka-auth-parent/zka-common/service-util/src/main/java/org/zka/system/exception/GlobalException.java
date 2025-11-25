package org.zka.system.exception;

import com.aliyuncs.exceptions.ClientException;
import org.zka.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.security.access.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;


@Slf4j
@ControllerAdvice
public class GlobalException {
    //1.全局异常处理  只要出现异常，就执行这个处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行了全局异常处理...");
    }

    //2.特定异常处理  针对 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        log.info("特定异常处理触发了");
        return Result.fail().message(e.getMessage());
    }
    //2.特定异常处理  针对 重复添加用户异常
    // 处理Spring包装后的"数据完整性约束冲突"异常（包含重复键等情况）
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result error(DataIntegrityViolationException e) {
        // 获取原始异常（nested exception）
        Throwable cause = e.getCause();
        // 判断原始异常是否为SQLIntegrityConstraintViolationException（重复键异常）
        if (cause instanceof SQLIntegrityConstraintViolationException) {
            log.info("重复添加用户异常触发了");
            return Result.fail().message("用户名已被使用！");
        }
        // 其他数据完整性异常（如非空约束等）
        return Result.fail().message("数据完整性约束异常");
    }

    //2.特定异常处理  针对 视频不存在异常
    @ExceptionHandler(ClientException.class)
    @ResponseBody
    public Result error(ClientException e){
        log.info("该影视未提交视频异常触发了");
        return Result.fail().message("该影视未提交视频！");
    }
    //2.特定异常处理  针对 没有权限
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e){
        log.info("没有权限异常触发了");
        return Result.fail().message("您没有权限！");
    }

    //自定义异常处理  自己编写异常类  手动抛出异常
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e){
        log.info("自定义异常触发了");
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }
}
