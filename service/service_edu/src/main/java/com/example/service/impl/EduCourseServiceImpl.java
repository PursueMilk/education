package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.EduCourse;
import com.example.entity.EduCourseDescription;
import com.example.entity.frontVo.CourseQueryVo;
import com.example.entity.vo.CourseInfoVo;
import com.example.entity.vo.CoursePublishVo;
import com.example.entity.frontVo.CourseWebVo;
import com.example.exceptionhandler.GuliException;
import com.example.mapper.EduCourseMapper;
import com.example.service.EduChapterService;
import com.example.service.EduCourseDescriptionService;
import com.example.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduChapterService eduChapterService;


    /**
     * 添加课程信息
     * @param courseInfoVo
     * @return
     */
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //向课程表添加课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0) {
            throw new GuliException(20001, "添加课程信息失败");
        }
        //获取保存后的id，与课程描述建立关系
        String CourseId = eduCourse.getId();
        //向课程简介添加课程简介
        EduCourseDescription description = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, description);
        description.setId(CourseId);
        eduCourseDescriptionService.save(description);
        return CourseId;
    }


    /**
     * 查询课程信息
     * @param courseId
     * @return
     */
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1、查询课程表类容
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //封装到CourseInfoVo中
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        //2、查询描述表
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }

    /**
     * 更新课程信息
     * @param courseInfoVo
     */
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1 修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }
        //2 修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(description);
    }

    /**
     *  删除课程包含（课程下的小节、视频）
     * @param courseId
     */
    @Override
    public void removeCourse(String courseId) {
        //根据课程id删除小节和视频
        eduVideoService.removeByCourseId(courseId);
        //根据课程id删除章节
        eduChapterService.removeChapterById(courseId);
        //根据课程id删除课程描述
        eduCourseDescriptionService.removeById(courseId);
        //根据课程id删除课程本身
        int i = baseMapper.deleteById(courseId);
        if (i == 0) {
            throw new GuliException(20001, "删除失败");
        }
    }

    /**
     * 查询出版信息
     * @param id
     * @return
     */
    @Override
    public CoursePublishVo getPublishCourseInfo (String id){
        //调用mapper
        CoursePublishVo coursePublishVo = baseMapper.getPublishCourseInfo(id);
        return coursePublishVo;
    }

    /**
     *
     * @param queryVoPage
     * @param courseQueryVo
     * @return
     */
    @Override
    public Map<String, Object> getTeacherInfo(Page<EduCourse> queryVoPage, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //判断条件是否为空
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){//一级分类
            queryWrapper.eq("subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectId())){//二级分类
            queryWrapper.eq("subject_id",courseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())) {//销量排序
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())) {//时间排序
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(courseQueryVo.getPriceSort())) {//价格排序
            queryWrapper.orderByDesc("price");
        }

        //封装到page里面
        baseMapper.selectPage(queryVoPage, queryWrapper);
        long total = queryVoPage.getTotal();
        List<EduCourse> records = queryVoPage.getRecords();
        long current = queryVoPage.getCurrent();
        long size = queryVoPage.getSize();
        boolean hasNext = queryVoPage.hasNext();
        boolean hasPrevious = queryVoPage.hasPrevious();
        long pages = queryVoPage.getPages();

        HashMap<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
