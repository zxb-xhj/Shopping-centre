package com.xhj.member.exception;

/**
 * @Author: xhj
 * @Date: 2023/05/14/22:28
 * @Description:
 */
public class PhoneException extends RuntimeException {
    public PhoneException(){
        super("手机号已存在");
    }

}
