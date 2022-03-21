package com.endless.video.constants;

/**
 * @ClassName Constants
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/16 21:58
 * @Version 1.0
 */
public interface Constants {

    interface SPKey {

        String USER_ID = "USER_ID";

        String USER_PHONE = "USER_PHONE";

        String SOURCE = "SOURCE";

        String SOURCE_LIST = "SOURCE_LIST";
    }

    interface Group {
        //默认资源
        int DEFAULT = 0;
        //需要解析
        int ANALYSIS = 1;

        int NORMAL = 2;

        int SPECIAL = 3;
    }


}
