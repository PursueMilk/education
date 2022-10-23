package com.example.mapper;

import com.example.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.EduCourse;
import com.example.entity.vo.CoursePublishVo;
import com.example.entity.frontVo.CourseWebVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {


    CoursePublishVo getPublishCourseInfo(String id);

    CourseWebVo getBaseCourseInfo(String courseId);

}
