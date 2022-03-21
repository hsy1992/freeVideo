package com.endless.video.core.bean;

import java.io.Serializable;

/**
 * @ClassName SourceDTO
 * @Description 资源实体类
 * @Author haosiyuan
 * @Date 2022/2/18 11:43
 * @Version 1.0
 */
public class SourceDTO implements Serializable {

    private String name;
    private String logo;
    private String desc;
    private Integer active;
    private String parseurl;
    private String root;
    private Integer sId;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getParseurl() {
        return parseurl;
    }

    public void setParseurl(String parseurl) {
        this.parseurl = parseurl;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }
}
