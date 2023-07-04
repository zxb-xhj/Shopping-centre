package com.xhj.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Author: xhj
 * @Date: 2023/05/14/22:29
 * @Description:
 */
@Data
public class MemberUserRegisterVo {

    private String userName;

    private String password;

    private String phone;
}


