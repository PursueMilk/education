package com.example.service;

import com.example.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVoByCourseId(String courseId);

    boolean deleteChapterById(String chapterId);

    void removeChapterById(String courseId);
}
