package com.endless.video.core.api.k4ya;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName K4YaDetailsTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/1/3 13:20
 * @Version 1.0
 */
public class K4YaDetailsTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private String video_id;

    public K4YaDetailsTO(String video_id) {
        this.video_id = video_id;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.11.0");
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    @Override
    public String getApi() {
        return "v2/video";
    }
}
