package com.xhj.member.dao;

import com.xhj.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:37:26
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
