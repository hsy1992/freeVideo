package com.endless.video.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ConfigDTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/17 14:03
 * @Version 1.0
 */
public class ConfigDTO implements Serializable {

    private List<AnimeDTO> anime;

    public List<AnimeDTO> getAnime() {
        return anime;
    }

    public void setAnime(List<AnimeDTO> anime) {
        this.anime = anime;
    }

    public static class AnimeDTO implements Serializable {
        /**
         * name : 阿房影视
         * notes : 资源质量非常好
         * module : api.anime.afang
         * type : ["综合"]
         * enable : true
         * quality : 9
         */

        private String name;
        private String notes;
        private String module;
        private boolean enable;
        private int quality;
        private List<String> type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }
    }
}
