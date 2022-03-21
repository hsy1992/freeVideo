package com.endless.video.utl;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.endless.video.constants.Constants;
import com.endless.video.ui.activity.LoginActivity;

/**
 * @ClassName ResultCodeUtil
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 12:13
 * @Version 1.0
 */
public class ResultCodeUtil {

    public static boolean checkCode(int code) {
        if (code == 200) return true;
        if (code == 401) {
            ActivityUtils.finishAllActivities();
            ActivityUtils.startActivity(LoginActivity.class);
            SPUtils.getInstance().put(Constants.SPKey.USER_ID, "");
        }
        return false;
    }
}
