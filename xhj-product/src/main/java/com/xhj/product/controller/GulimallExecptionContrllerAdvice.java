package com.xhj.product.controller;

import com.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.MarshalException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xhj
 * @Date: 2023/03/14/14:23
 * @Description:
 */
@RestControllerAdvice(value = "com.xhj.product.controller")
@Slf4j
public class xhjExecptionContrllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildExceptionAdvice(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        log.error("数据校验失败"+e.getMessage(),e.getClass());
        Map<String,String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((FieldError)->{
            map.put(FieldError.getField(), FieldError.getDefaultMessage());
        });
        return R.error(400,"数据校验出错").put("data",map);
    }

}
