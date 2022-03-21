package com.endless.video.core.bean.mirror;

import java.util.List;

/**
 * @ClassName MirrorOnceItemSerialize
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/2/18 13:56
 * @Version 1.0
 */
public class MirrorOnceItemSerialize {
    /// id
    private String id;

    /// 标题
    private String title;

    /// 介绍
    private String desc;

    /// 喜欢
    private int likeCount;

    /// 访问人数
    private int viewCount;

    /// 不喜欢
    private int dislikeCount;

    /// 小封面图(必须要有)
    private String smallCoverImage;

    /// 大封面图
    private String bigCoverImage;

    /// 视频列表
    private List<MirrorSerializeVideoInfo> videos;

    /// 视频信息
    /// 视频尺寸大小
    /// 视频长度大小
    private MirrorSerializeVideoSize videoInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getSmallCoverImage() {
        return smallCoverImage;
    }

    public void setSmallCoverImage(String smallCoverImage) {
        this.smallCoverImage = smallCoverImage;
    }

    public String getBigCoverImage() {
        return bigCoverImage;
    }

    public void setBigCoverImage(String bigCoverImage) {
        this.bigCoverImage = bigCoverImage;
    }

    public List<MirrorSerializeVideoInfo> getVideos() {
        return videos;
    }

    public void setVideos(List<MirrorSerializeVideoInfo> videos) {
        this.videos = videos;
    }

    public MirrorSerializeVideoSize getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(MirrorSerializeVideoSize videoInfo) {
        this.videoInfo = videoInfo;
    }
}
