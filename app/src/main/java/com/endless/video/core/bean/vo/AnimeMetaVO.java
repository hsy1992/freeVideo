package com.endless.video.core.bean.vo;

/**
 * @ClassName AnimeMetaVo
 * @Description 摘要信息
 * @Author haosiyuan
 * @Date 2021/12/17 17:26
 * @Version 1.0
 */
public class AnimeMetaVO {

    /**
     * 番剧标题
     */
    private String title;

    /**
     * 封面图片链接
     */
    private String coverUrl;

    /**
     * 番剧的分类
     */
    private String category;

    /**
     * 简介信息
     */
    private String desc;

    /**
     * 详情页面的链接或者相关参数
     */
    private String detailUrl;

    /**
     * 引擎module
     */
    private String module;

    public AnimeMetaVO(String title, String coverUrl, String category, String desc, String detailUrl, String module) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.category = category;
        this.desc = desc;
        this.detailUrl = detailUrl;
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
