package com.xhj.thirdparty;

import com.xhj.thirdparty.component.SmsComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: xhj
 * @Date: 2023/05/14/16:38
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class XhjThirdpartyApplicationTests {

    @Autowired
    SmsComponent smsComponent;

    @Test
    public void test01(){
        smsComponent.sendSmsCode("18570350735","code:7788");
    }
}
