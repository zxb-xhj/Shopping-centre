package com.xhj.thirdparty.controller;

import com..common.utils.R;
import com.xhj.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xhj
 * @Date: 2023/05/14/17:18
 * @Description:
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsComponent smsComponent;

    @GetMapping("/smscode")
    public R smsCoe(@RequestParam("phone") String phone,@RequestParam("code") String code){
//        smsComponent.sendSmsCode(phone, code);
        System.out.println("code="+code);
        return R.ok();
    }

}
