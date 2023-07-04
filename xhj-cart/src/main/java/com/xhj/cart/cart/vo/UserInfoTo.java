package com.xhj.cart.cart.vo;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/05/23/15:25
 * @Description:
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private Boolean isTempUser = false;
}
