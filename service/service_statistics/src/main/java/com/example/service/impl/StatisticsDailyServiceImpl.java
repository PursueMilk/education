package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.UcenterClient;
import com.example.commonutils.R;
import com.example.entity.StatisticsDaily;
import com.example.mapper.StatisticsDailyMapper;
import com.example.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-08-12
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void countRegister(String day) {
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);//如果同一天统计的，则先删除在统计
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer)registerR.getData().get("countRegister");

        //获取的数据添加到数据库
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);        //注册时间

        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(sta);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);//查询需要的列，数据时间和类型
        List<StatisticsDaily> lists = baseMapper.selectList(queryWrapper);

        //返回的数据有两部分：date_calculated     和      所类型type
        //前端需要json数据，对于java代码的list集合，需要返回x、y轴两个list数据
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> typeList = new ArrayList<>();
        //遍历lists封装两个list集合
        for (StatisticsDaily list:lists){
            dateList.add(list.getDateCalculated());//封装日期

            if (type.equals("login_num")){//封装数量类型
                typeList.add(list.getLoginNum());
            }
            if (type.equals("register_num")){
                typeList.add(list.getRegisterNum());
            }
            if (type.equals("video_view_num")){
                typeList.add(list.getVideoViewNum());
            }
            if (type.equals("course_num")){
                typeList.add(list.getCourseNum());
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",dateList);
        map.put("numDataList",typeList);
        return map;
    }
}
