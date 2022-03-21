package com.endless.video.core.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName NormalXmlData
 * @Description xml解析数据
 * @Author haosiyuan
 * @Date 2022/2/18 16:14
 * @Version 1.0
 */
public class NormalXmlData {

    private String version;
    private MovieList list;

    private JSONObject rss;

    public NormalXmlData(JSONObject rss) {
        this.rss = rss;
        version = rss.getString("@version");
        JSONObject movieList = rss.getJSONObject("list");
        if (movieList != null) {
            list = new MovieList(movieList);
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MovieList getList() {
        return list;
    }

    public void setList(MovieList list) {
        this.list = list;
    }

    public static class MovieList {
        private List<VideoData> video;
        private String page;
        private String pagecount;
        private String pagesize;
        private String recordcount;

        public MovieList(JSONObject movieList) {
            Object videoObj = movieList.get("video");
            if (videoObj != null) {
                video = new ArrayList<>();
                if (videoObj instanceof JSONObject) {
                    JSONObject videoJObj = (JSONObject) videoObj;
                    video.add(autoFix2String(videoJObj));
                } else {
                    JSONArray videoJsonArray = (JSONArray) videoObj;
                    for (int i = 0; i < videoJsonArray.size(); i++) {
                        JSONObject videoJsonObj = videoJsonArray.getJSONObject(i);
                        video.add(autoFix2String(videoJsonObj));
                    }
                }
            }
        }

        private VideoData autoFix2String(JSONObject videoJObj) {
            VideoData videoData = new VideoData();
            videoData.setLast(videoJObj.getString("last"));
            videoData.setId(videoJObj.getString("id"));
            videoData.setTid(videoJObj.getString("tid"));
            videoData.setName(videoJObj.getString("name"));
            videoData.setType(videoJObj.getString("type"));
            videoData.setPic(videoJObj.getString("pic"));
            videoData.setLang(videoJObj.getString("lang"));
            videoData.setArea(videoJObj.getString("area"));
            videoData.setYear(videoJObj.getString("year"));
            videoData.setState(videoJObj.getString("state"));
            videoData.setNote(videoJObj.getString("note"));
            videoData.setActor(videoJObj.getString("actor"));
            videoData.setDirector(videoJObj.getString("director"));
            videoData.setDes(videoJObj.getString("des"));
            JSONObject dl = videoJObj.getJSONObject("dl");
            if (!ObjectUtils.isEmpty(dl)) {
                videoData.setDl(dl.getString("dd"));
            }
            return videoData;
        }

        public List<VideoData> getVideo() {
            return video;
        }

        public void setVideo(List<VideoData> video) {
            this.video = video;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPagecount() {
            return pagecount;
        }

        public void setPagecount(String pagecount) {
            this.pagecount = pagecount;
        }

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public String getRecordcount() {
            return recordcount;
        }

        public void setRecordcount(String recordcount) {
            this.recordcount = recordcount;
        }
    }

    public static class VideoData {
        private String last;
        private String id;
        private String tid;
        private String name;
        private String type;
        private String pic;
        private String lang;
        private String area;
        private String year;
        private String state;
        private String note;
        private String actor;
        private String director;
        private String dl;
        private String des;

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getDl() {
            return dl;
        }

        public void setDl(String dl) {
            this.dl = dl;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }


}
