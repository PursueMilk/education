package com.example.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.example.exceptionhandler.GuliException;
import com.example.utils.AliyunVodSDKUtils;
import com.example.utils.ConstantVodUtils;
import com.example.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            //fileName：上传文件原始名称
            String fileName = file.getOriginalFilename();
            //title：上传之后显示名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            // 4.流式上传，如文件流和网络流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeVideo(String videoId) {
        try {
            //初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request中设置videoId
            request.setVideoIds(videoId);
            //调用删除方法
            DeleteVideoResponse response = client.getAcsResponse(request);

            log.info("RequestId = {}", response.getRequestId());
        } catch (Exception e) {
            throw new GuliException(20001, "视频删除失败");
        }
    }

    @Override
    public void removeAllVideo(List videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);
            String list = StringUtils.join(videoIdList.toArray(), ",");
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request中设置videoId
            request.setVideoIds(list);
            //调用删除方法
            DeleteVideoResponse response = client.getAcsResponse(request);

            log.info("RequestId = {}", response.getRequestId());
        } catch (Exception e) {
            throw new GuliException(20001, "视频删除失败");
        }
    }
}
