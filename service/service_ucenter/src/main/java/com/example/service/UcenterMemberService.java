package com.example.service;

import com.example.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-09
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    Integer ucenterMemberService(String day);
}
