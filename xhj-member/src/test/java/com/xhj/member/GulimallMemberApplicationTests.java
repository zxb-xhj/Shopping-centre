package com.xhj.member;

import com.alibaba.fastjson.TypeReference;
import com..common.utils.R;
import com..common.vo.MemberResponseVo;
import com.xhj.member.controller.MemberController;
import com.xhj.member.entity.MemberLevelEntity;
import com.xhj.member.service.MemberLevelService;
import com.xhj.member.service.MemberService;
import com.xhj.member.vo.MemberUserLoginVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class xhjMemberApplicationTests {

    @Autowired
    MemberLevelService memberLevelService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberController memberController;

    @Test
    void contextLoads() {
        List<MemberLevelEntity> list = memberLevelService.list();
        list.forEach((member)->{
            System.out.println(member);
        });
    }

    @Test
    void test1() {
        MemberUserLoginVo loginVo = new MemberUserLoginVo();
        loginVo.setLoginacct("18570350735");
        loginVo.setPassword("123456");
        R r = memberController.login(loginVo);
        System.out.println(r.getData("data",new TypeReference<MemberResponseVo>(){}));
    }

}
