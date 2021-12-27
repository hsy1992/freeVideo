package com.endless.video.core.api.bimi;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BiMiUrlTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/23 9:06
 * @Version 1.0
 */
public class BiMiUrlTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private String raw_url;

    public BiMiUrlTO(String raw_url) {
        this.raw_url = raw_url;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.14.2");
        headers.put("appid", "4150439554430555");
    }

    @Override
    public String getApi() {
        return "app/video/play" + raw_url;
    }
}
