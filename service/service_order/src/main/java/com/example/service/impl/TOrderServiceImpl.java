package com.example.service.impl;

import com.example.client.EduClient;
import com.example.client.UcenterClient;
import com.example.commonutils.user.CourseWebVoOrder;
import com.example.commonutils.user.UcenterMemberOrder;
import com.example.entity.TOrder;
import com.example.mapper.TOrderMapper;
import com.example.utils.OrderNoUtil;
import com.example.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-11
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //1.生成订单的方法
    @Override
    public String createOrders(String courseId, String memberId) {
        //远程调用根据课程id获得课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //远程调用根据用户id获得用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //创建order对象，向order中设置需要的值
        TOrder order = new TOrder();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);

        //返回订单号
        return order.getOrderNo();
    }
}
