package com.example.service;

import com.example.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-11
 */
public interface TOrderService extends IService<TOrder> {

    String createOrders(String courseId, String memberId);
}
