package com.example.exceptionhandler;



import com.example.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指出什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public R error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody//为了返回数据
    public R guliError(GuliException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().message(e.getMsg());
    }
}
