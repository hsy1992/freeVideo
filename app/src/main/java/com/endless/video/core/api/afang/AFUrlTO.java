package com.endless.video.core.api.afang;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AFUrlTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/20 13:27
 * @Version 1.0
 */
public class AFUrlTO implements IRequestApi {

    private String source_id;
    private String chapter_id;
    private String video_id;

    @HttpHeader
    private Map<String, String> headers;

    public AFUrlTO() {
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/4.1.0");
    }

    public AFUrlTO setSource_id(String source_id) {
        this.source_id = source_id;
        return this;
    }

    public AFUrlTO setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
        return this;
    }

    public AFUrlTO setVideo_id(String video_id) {
        this.video_id = video_id;
        return this;
    }

    @Override
    public String getApi() {
        return "video/parse";
    }
}
