package com.example.client;

import com.example.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoClientImpl implements VideoClient {
    @Override
    public R removeVideo(String videoId) {
        return R.error().message("删除视频出错");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错");
    }
}
