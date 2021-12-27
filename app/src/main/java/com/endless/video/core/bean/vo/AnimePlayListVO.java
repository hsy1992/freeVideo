package com.endless.video.core.bean.vo;

import com.blankj.utilcode.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName AnimePlayListVo
 * @Description 播放列表
 * @Author haosiyuan
 * @Date 2021/12/17 17:25
 * @Version 1.0
 */
public class AnimePlayListVO {

    /**
     * 播放列表名, 比如 "播放线路1"
     */
    private String name;

    /**
     * 视频集数, 不确定时为0
     */
    private int num;

    private String module;

    private List<AnimeVO> animeVoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return CollectionUtils.isEmpty(animeVoList) ? 0 : animeVoList.size();
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<AnimeVO> getAnimeVoList() {
        return animeVoList;
    }

    public void setAnimeVoList(List<AnimeVO> animeVoList) {
        this.animeVoList = animeVoList;
    }
}
