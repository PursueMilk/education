package com.example.client;

import com.example.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod", fallback = VideoClientImpl.class)
@Component
public interface VideoClient {
    @DeleteMapping("/eduvod/video/removeVideo/{videoId}")
    public R removeVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam List<String> videoIdList);
}
