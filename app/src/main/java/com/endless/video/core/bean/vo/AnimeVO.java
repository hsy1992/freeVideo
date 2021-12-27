package com.endless.video.core.bean.vo;

/**
 * @ClassName AnimeVo
 * @Description 单集视频对象
 * @Author haosiyuan
 * @Date 2021/12/17 17:25
 * @Version 1.0
 */
public class AnimeVO {

    private String name;
    private String raw_url;
    private String module;

    public AnimeVO(String name, String raw_url, String module) {
        this.name = name;
        this.raw_url = raw_url;
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRaw_url() {
        return raw_url;
    }

    public void setRaw_url(String raw_url) {
        this.raw_url = raw_url;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
