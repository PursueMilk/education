package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.entity.TOrder;
import com.example.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-08-11
 */
@RestController
@RequestMapping("/eduorder/order")
public class TOrderController {
    @Autowired
    private TOrderService tOrderService;

    //1.生成订单的方法
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //创建订单，返回订单号
        String orderNo = tOrderService.createOrders(courseId,memberId);
        return R.ok().data("orderId",orderNo);
    }

    //2.根据订单id查询
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderId);
        TOrder orderInfo = tOrderService.getOne(queryWrapper);
        return R.ok().data("item",orderInfo);
    }

    //3.根据课程Id和用户ID查询订单表中的订单状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable(value = "courseId") String courseId,
                               @PathVariable(value = "memberId") String memberId){
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);
        int count = tOrderService.count(queryWrapper);
        if (count>0){
            return true;
        }else {
            return false;
        }
    }

}

