package com.endless.video.core.api.normal;

import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NormalSearchApi
 * @Description search
 * @Author haosiyuan
 * @Date 2022/2/20 21:51
 * @Version 1.0
 */
public class NormalSearchApi implements IRequestApi {

    private String ac;
    private String wd;
    private int pg;
    @HttpHeader
    private Map<String, String> header = new HashMap<>();

    public NormalSearchApi(int pg, String keyword) {
        header.put("Content-Type", "text/plain");
        this.ac = "videolist";
        this.pg = pg;
        this.wd = keyword;
    }

    @Override
    public String getApi() {
        return "";
    }
}
