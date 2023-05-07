package com.atguigu.gulimall.member;

import com.atguigu.gulimall.member.entity.MemberLevelEntity;
import com.atguigu.gulimall.member.service.MemberLevelService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class GulimallMemberApplicationTests {

    @Autowired
    MemberLevelService memberLevelService;

    @Test
    void contextLoads() {
        List<MemberLevelEntity> list = memberLevelService.list();
        list.forEach((member)->{
            System.out.println(member);
        });
    }

}
