package com.endless.video.core.api.afang;

import com.blankj.utilcode.util.StringUtils;
import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AFSearchTO
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/19 22:29
 * @Version 1.0
 */
public class AFSearchTO implements IRequestApi {

    private String page_num;
    private String keyword;
    private String page_size;
    @HttpHeader
    private Map<String, String> headers;

    public AFSearchTO() {
        headers = new HashMap<>();
        headers.put("User-Agent", "okhttp/4.1.0");
    }

    public AFSearchTO setPageNum(String page_num) {
        this.page_num = page_num;
        return this;
    }

    public AFSearchTO setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public AFSearchTO setPageSize(String page_size) {
        this.page_size = page_size;
        return this;
    }

    @Override
    public String getApi() {
        return "search/result";
    }

    public final static class Bean {

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {

            private List<ListBean> list;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {

                private String cover;
                private String video_name;
                private String video_id;
                private String intro;
                private String category;

                public String getCover() {
                    return cover;
                }

                public String getVideo_name() {
                    return video_name;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public String getIntro() {
                    return intro;
                }

                public String getCategory() {
                    return StringUtils.isEmpty(category) ? "无简介" : category;
                }
            }
        }
    }
}
