package com.endless.video.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * @ClassName AppRegister
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/19 23:08
 * @Version 1.0
 */
public class AppRegisterApi implements IRequestApi {

    private String name;
    private String password;

    public AppRegisterApi(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getApi() {
        return "user/register";
    }

}
