package com.example.controller;


import com.example.commonutils.R;
import com.example.entity.subject.OneSubject;
import com.example.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService subjectService;

    //添加课程分类
    //获取上传过来文件，把文件内容读取出来
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file) {
        if (file==null){
            return R.error();
        }
        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }


    //课程分类返回结构(树形)
    @GetMapping("/getAllSubject")
    public R getAllSubject() {
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list", list);
    }

}

