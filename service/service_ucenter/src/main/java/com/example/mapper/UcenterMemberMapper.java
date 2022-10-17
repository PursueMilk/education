package com.example.mapper;

import com.example.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author unicorn
 * @since 2022-08-09
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    //查询某一天注册人数
    Integer countRegisterDay(String day);
}
