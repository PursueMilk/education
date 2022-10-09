package com.example.controller;


import com.example.commonutils.R;
import com.example.entity.CrmBanner;
import com.example.entity.CrmBanner;
import com.example.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端系统轮播图
 *
 * @author unicorn
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

    //查询所有banner
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> List =crmBannerService.selectAllBanner();
        return R.ok().data("list",List);
    }

}

