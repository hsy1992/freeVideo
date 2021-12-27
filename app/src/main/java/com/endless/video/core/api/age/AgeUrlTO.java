package com.endless.video.core.api.age;

import com.hjq.http.config.IRequestApi;

/**
 * @ClassName AgeUrlTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/20 16:03
 * @Version 1.0
 */
public class AgeUrlTO implements IRequestApi {

    private String playid;
    private String vid;
    private String kt;
    private String kp;

    public AgeUrlTO(String playid, String vid, String kt, String kp) {
        this.playid = playid;
        this.vid = vid;
        this.kt = kt;
        this.kp = kp;
    }

    @Override
    public String getApi() {
        return "";
    }
}
