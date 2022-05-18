package com.lyc.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Date: 2022/5/16
 * Author: 3378
 * Description:
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {


    /**
     * 处理controller层中出现的异常
     * @param exception 异常对象
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        String message = exception.getMessage();
        log.error(message);

        if(message.contains("Duplicate entry")){
            String[] strings = message.split(" ");
            return R.error(strings[2]+"该账号已存在");
        }
        return R.error("创建失败");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandle(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }



}
