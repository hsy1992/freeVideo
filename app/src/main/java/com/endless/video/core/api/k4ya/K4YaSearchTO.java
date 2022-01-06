package com.endless.video.core.api.k4ya;

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
public class K4YaSearchTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private int start = 0;
    private int num = 10;
    private boolean paging = true;
    private String key;

    public K4YaSearchTO(String keyword) {
        this.key = keyword;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.11.0");
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getApi() {
        return "/v2/videos";
    }
}