package com.endless.video.core.bean;

import java.io.Serializable;

/**
 * @ClassName TvDTO
 * @Description 电视资源
 * @Author haosiyuan
 * @Date 2022/2/18 13:32
 * @Version 1.0
 */
public class TvDTO implements Serializable {

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
