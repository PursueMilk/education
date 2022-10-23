package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.client.VideoClient;
import com.example.commonutils.R;
import com.example.entity.EduVideo;
import com.example.exceptionhandler.GuliException;
import com.example.mapper.EduVideoMapper;
import com.example.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VideoClient videoClient;

    //根据课程id删除小节和视频

    /**
     * 根据课程id删除小节和视频，课程不删除
     * 优化：添加查询条件，不需要再次筛选
     * @param courseId
     */
    @Override
    public void removeByCourseId(String courseId) {
        //根据课程id查出所有视频的id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);
        //封装video_source_id  1,2,3
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!videoSourceId.isEmpty()) {
                list.add(videoSourceId);
            }
        }
        //list不为空
        if (list.size() > 0) {
            //删除小节里的所有视频
            R result = videoClient.deleteBatch(list);
            if (result.getCode() == 20001) {
                throw new GuliException(20001, "删除视频失败，熔断器...");
            }
        }
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
