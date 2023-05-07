package com.atguigu.gulimall.product.exception;

import com.atguigu.common.exception.BizCodeEnum;
import com.atguigu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xhj
 * @Date: 2023/03/22/22:00
 * @Description:
 */
//@RestControllerAdvice
@Slf4j
public class GulimallExceptionController {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.info("MethodArgumentNotValidException");
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errormap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            errormap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data",errormap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R Exception(Throwable e){
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMsg()).put("msg",e.getLocalizedMessage());
    }
}
