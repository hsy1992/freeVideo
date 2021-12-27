package com.endless.video.core.bean.vo;

import java.util.List;

/**
 * @ClassName AnimeDetailVo
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/17 17:26
 * @Version 1.0
 */
public class AnimeDetailsVO {

    private String title;
    private String cover_url;
    private String category;
    private String desc;
    private String module;
    private List<AnimePlayListVO> playlists;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<AnimePlayListVO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<AnimePlayListVO> playlists) {
        this.playlists = playlists;
    }
}
