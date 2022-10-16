package com.example.service;

import com.example.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-12
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void countRegister(String day);

    Map<String, Object> getShowData(String type, String begin, String end);
}
