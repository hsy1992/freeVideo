package com.endless.video.core.api.nf;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NFHomeApi
 * @Description 奈非影视
 * @Author haosiyuan
 * @Date 2022/2/18 14:15
 * @Version 1.0
 */
public class NFHomeApi implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers = new HashMap<>();
    private String api;

    public NFHomeApi(String api) {
        this.api = api;
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0");
    }

    @Override
    public String getApi() {
        return api;
    }
}
