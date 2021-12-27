package com.endless.video.core.api.afang;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AFDetailsTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/20 11:08
 * @Version 1.0
 */
public class AFDetailsTO implements IRequestApi {

    private String video_id;
    @HttpHeader
    private Map<String, String> headers;

    public AFDetailsTO() {
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/4.1.0");
    }

    public AFDetailsTO setVideoId(String video_id) {
        this.video_id = video_id;
        return this;
    }

    @Override
    public String getApi() {
        return "video/info";
    }
}
