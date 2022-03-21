package com.endless.video.http.api.app;

/**
 * @ClassName AppLoginApi
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 11:35
 * @Version 1.0
 */
public class AppLoginApi {

    private String name;
    private String password;

    public AppLoginApi(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public final static class Bean {

        private String token;

        public String getToken() {
            return token;
        }
    }
}
