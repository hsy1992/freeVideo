package com.endless.video.core.api.bimi;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BiMiSearchTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/22 20:42
 * @Version 1.0
 */
public class BiMiDetailsTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private String id;

    public BiMiDetailsTO(String id) {
        this.id = id;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.14.2");
        headers.put("appid", "4150439554430555");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getApi() {
        return "app/video/detail";
    }
}
