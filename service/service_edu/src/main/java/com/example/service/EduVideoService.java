package com.example.service;

import com.example.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);
}
