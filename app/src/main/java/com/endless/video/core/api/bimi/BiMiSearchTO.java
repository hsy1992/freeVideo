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
public class BiMiSearchTO implements IRequestApi {

    @HttpHeader
    private Map<String, String> headers;

    private String limit;
    private String keyword;
    private String page;

    public BiMiSearchTO(String limit, String keyword, String page) {
        this.limit = limit;
        this.keyword = keyword;
        this.page = page;
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/3.14.2");
        headers.put("appid", "4150439554430555");
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String getApi() {
        return "app/video/search";
    }
}
