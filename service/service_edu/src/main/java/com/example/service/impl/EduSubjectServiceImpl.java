package com.example.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.EduSubject;
import com.example.entity.excel.SubjectData;
import com.example.entity.subject.OneSubject;
import com.example.entity.subject.TwoSubject;
import com.example.listener.SubjectExcelListener;
import com.example.mapper.EduSubjectMapper;
import com.example.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author unicorn
 * @since 2022-07-28
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 添加一二级分类
     * @param file
     * @param eduSubjectService
     */
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        //获取输入流
        try {
            InputStream is = file.getInputStream();
            EasyExcel.read(is, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  返回一级标题+对应的二级标题  O(n*n)
     * @return
     */
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1查询出所有一级分类 parent_id=0
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper);

        //2查询出所有二级分类 parent_id!=0
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);

        //3封装一级分类
        List<OneSubject> finalList = new ArrayList<>();
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);

            //new OneSubject设置值，add加入list
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            //复制操作
            BeanUtils.copyProperties(eduSubject, oneSubject);

            finalList.add(oneSubject);
            //4封装二级分类
            //创建list集合封装每一个一级分类的二级分类
            ArrayList<TwoSubject> twoFinalList = new ArrayList<>();
            for (int j = 0; j < twoSubjectList.size(); j++) {
                EduSubject eduSubject2 = twoSubjectList.get(j);
                if (eduSubject.getId().equals(eduSubject2.getParentId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    //如过一级分类的id==二级分类的parent_id,进行封装
                    BeanUtils.copyProperties(eduSubject2, twoSubject);
                    twoFinalList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalList);
        }
        return finalList;
    }
}
