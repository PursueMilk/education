package com.example.controller;


import com.example.commonutils.R;
import com.example.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-08-12
 */
@RestController
@RequestMapping("/staservice/sta")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计一天的注册人数,生成统计数据
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.countRegister(day);
        return R.ok();
    }

    @GetMapping("showData/{type}/{begin}/{end}")
    public R getShowData(@PathVariable String type,@PathVariable String begin,@PathVariable String end){
        if ("undefined".equals(type)||"undefined".equals(begin)||"undefined".equals(end)){
            return R.error().message("参数不能为空");
        }
        Map<String,Object> map = statisticsDailyService.getShowData(type,begin,end);
        return R.ok().data(map);
    }
}

