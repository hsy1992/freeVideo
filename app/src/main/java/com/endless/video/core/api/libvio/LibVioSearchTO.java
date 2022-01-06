package com.endless.video.core.api.libvio;

import com.hjq.http.config.IRequestApi;

/**
 * @ClassName K4YaSearchTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/1/3 13:01
 * @Version 1.0
 */
public class LibVioSearchTO implements IRequestApi {

    private String submit = "";
    private String wd;

    public LibVioSearchTO(String wd) {
        this.wd = wd;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    @Override
    public String getApi() {
        return "";
    }
}