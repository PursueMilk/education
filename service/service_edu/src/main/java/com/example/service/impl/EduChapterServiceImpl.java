package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.EduChapter;
import com.example.entity.EduVideo;
import com.example.entity.chapter.ChapterVo;
import com.example.entity.chapter.VideoVo;
import com.example.exceptionhandler.GuliException;
import com.example.mapper.EduChapterMapper;
import com.example.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    /**
     * 先查出章节再查询小节、两个再匹配 O（n*n）
     * 优化：查出章节、用章节直接查询小节 O（n）
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterVoByCourseId(String courseId) {
        //1根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(chapterQueryWrapper);
        //2根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(videoQueryWrapper);
        //创建list集合进行最终封装
        ArrayList<ChapterVo> finalList = new ArrayList<>();
        //3遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            finalList.add(chapterVo);

            ArrayList<VideoVo> finalVideoVoList = new ArrayList<>();
            //4遍历查询小节list集合进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                EduVideo eduVideo = eduVideoList.get(j);
                if (eduVideo.getChapterId().equals(chapterVo.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    finalVideoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(finalVideoVoList);
        }
        return finalList;
    }

    /**
     * 删除章节，章节下面存在小结则删除失败
     * @param chapterId
     * @return
     */
    @Override
    public boolean deleteChapterById(String chapterId) {
        //根据章节id，如果有小节数据则不能删除
        QueryWrapper<EduVideo> eduVideoWrapper = new QueryWrapper<>();
        eduVideoWrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(eduVideoWrapper);
        if (count > 0) {
            //有小节
            throw new GuliException(20001, "有小节，无法删除");
        }
        int i = baseMapper.deleteById(chapterId);
        return i > 0;
    }


    /**
     * 删除课程，不能进行递归，不能删除小节
     * @param courseId
     */
    @Override
    public void removeChapterById(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
