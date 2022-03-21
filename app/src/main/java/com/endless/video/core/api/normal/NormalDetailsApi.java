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
public class NormalDetailsApi implements IRequestApi {

    private String ac;
    private String ids;
    @HttpHeader
    private Map<String, String> header = new HashMap<>();

    public NormalDetailsApi(String movie_id) {
        header.put("Content-Type", "text/plain");
        this.ac = "videolist";
        this.ids = movie_id;
    }

    @Override
    public String getApi() {
        return "";
    }
}
