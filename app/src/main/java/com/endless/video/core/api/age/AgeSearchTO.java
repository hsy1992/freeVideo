package com.endless.video.core.api.age;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.annotation.HttpIgnore;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AgeSearchTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/20 14:23
 * @Version 1.0
 */
public class AgeSearchTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;
    @HttpIgnore
    private String keyword;
    @HttpIgnore
    private int page;

    public AgeSearchTO(String keyword, int page) {
        this.keyword = keyword;
        this.page = page;
        headers = new HashMap<>();
        headers.put("Origin", "https://web.age-spa.com:8443");
        headers.put("Referer", "https://web.age-spa.com:8443/");
    }

    @Override
    public String getApi() {
        return String.format("v2/search?page=%d&query=%s", page, keyword);
    }
}
