package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.frontVo.CourseQueryVo;
import com.example.entity.vo.CourseInfoVo;
import com.example.entity.vo.CoursePublishVo;
import com.example.entity.frontVo.CourseWebVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    void removeCourse(String courseId);

    CoursePublishVo getPublishCourseInfo(String id);

    Map<String,Object> getTeacherInfo(Page<EduCourse> queryVoPage, CourseQueryVo courseQueryVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
