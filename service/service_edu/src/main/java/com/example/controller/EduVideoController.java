package com.example.controller;


import com.example.client.VideoClient;
import com.example.commonutils.R;
import com.example.entity.EduVideo;
import com.example.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VideoClient videoClient;


    //添加小节信息
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //刪除小节
    //删除小节同时把小节中的视频删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据id查询出视频id，进行删除
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoId)) {
            videoClient.removeVideo(videoId);
        }
        //删除小节
        eduVideoService.removeById(id);
        return R.ok();
    }


}

