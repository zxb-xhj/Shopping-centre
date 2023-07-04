package com.xhj.member.exception;

/**
 * @Author: xhj
 * @Date: 2023/05/14/22:29
 * @Description:
 */
// 继承 RuntimeException 表示运行异常
public class UsernameException extends RuntimeException {
    public UsernameException(){
        super("用户已存在");
    }
}
