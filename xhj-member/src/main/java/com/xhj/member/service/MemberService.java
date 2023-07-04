package com.xhj.member.service;

import com.xhj.member.exception.PhoneException;
import com.xhj.member.exception.UsernameException;
import com.xhj.member.vo.MemberUserLoginVo;
import com.xhj.member.vo.MemberUserRegisterVo;
import com.xhj.member.vo.SocialUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com..common.utils.PageUtils;
import com.xhj.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:37:26
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户注册
     * @param vo
     */
    void register(MemberUserRegisterVo vo);

    /**
     * 判断邮箱是否重复
     * @param phone
     * @return
     */
    void checkPhoneUnique(String phone) throws PhoneException;

    /**
     * 判断用户名是否重复
     * @param userName
     * @return
     */
    void checkUserNameUnique(String userName) throws UsernameException;

    MemberEntity login(MemberUserLoginVo vo);

    /**
     * 微博登录注册功能
     * @param socialUser
     * @return
     * @throws Exception
     */
    MemberEntity login(SocialUser socialUser) throws Exception;
}

