package com.endless.video.core.api.normal;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.annotation.HttpIgnore;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NormalHomeApi
 * @Description 通用首页api
 * @Author haosiyuan
 * @Date 2022/2/18 15:11
 * @Version 1.0
 */
public class NormalHomeApi implements IRequestApi {

    private String ac;
    private int pg;
    @HttpHeader
    private Map<String, String> header = new HashMap<>();

    public NormalHomeApi(String ac, int pg) {
        header.put("Content-Type", "text/plain");
        this.ac = ac;
        this.pg = pg;
    }

    @Override
    public String getApi() {
        return "";
    }
}
