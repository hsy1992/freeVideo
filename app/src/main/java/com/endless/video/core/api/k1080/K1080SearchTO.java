package com.endless.video.core.api.k1080;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName K4YaSearchTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/1/3 13:01
 * @Version 1.0
 */
public class K1080SearchTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private String page_num = "1";
    private String page_size = "12";
    private String keyword;

    public K1080SearchTO(String keyword) {
        this.keyword = keyword;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/4.1.0");
    }

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getApi() {
        return "/search/result";
    }
}