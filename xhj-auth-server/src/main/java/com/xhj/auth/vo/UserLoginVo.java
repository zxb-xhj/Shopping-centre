package com.xhj.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: xhj
 * @Date: 2023/05/15/14:07
 * @Description:
 */
@Data
public class UserLoginVo {
    private String loginacct;
    private String password;
}
