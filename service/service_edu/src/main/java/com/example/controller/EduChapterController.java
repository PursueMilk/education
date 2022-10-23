package com.example.controller;


import com.example.commonutils.R;
import com.example.entity.EduChapter;
import com.example.entity.chapter.ChapterVo;
import com.example.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //根据id查询课程信息
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVoByCourseId(courseId);
        return R.ok().data("allChapterVideo", list);
    }

    //添加课程章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return R.ok();
    }

    //查询根据id课程章节
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId) {
        EduChapter chapterById = chapterService.getById(chapterId);
        return R.ok().data("chapter", chapterById);
    }


    //修改课程章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除课程章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId) {
        boolean result = chapterService.deleteChapterById(chapterId);
        if (result) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

