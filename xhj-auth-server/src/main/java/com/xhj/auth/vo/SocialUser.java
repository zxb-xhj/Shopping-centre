package com..xhj.auth.vo;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/05/16/14:11
 * @Description:
 */

@Data
public class SocialUser {

    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;

}
