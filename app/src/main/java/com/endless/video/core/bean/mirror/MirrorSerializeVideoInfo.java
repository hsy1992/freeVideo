package com.endless.video.core.bean.mirror;

import android.text.TextUtils;

import com.endless.video.core.VideoType;

/**
 * @ClassName MirrorSerializeVideoInfo
 * @Description 列表数据
 * @Author haosiyuan
 * @Date 2022/2/18 13:55
 * @Version 1.0
 */
public class MirrorSerializeVideoInfo {

    private String name;

    private VideoType type;

    private String url;

    public MirrorSerializeVideoInfo(String name, VideoType type, String url) {
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public String getName() {
        return TextUtils.isEmpty(name) ? "未命名" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VideoType getType() {
        return type;
    }

    public void setType(VideoType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
