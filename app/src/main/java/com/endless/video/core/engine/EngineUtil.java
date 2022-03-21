package com.endless.video.core.engine;

import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.interfaces.OnControllerCallBack;

/**
 * @ClassName EngineUtil
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 21:27
 * @Version 1.0
 */
public class EngineUtil {

    private static class SingletonHolder {
        private static EngineUtil INSTANCE = new EngineUtil();
    }

    public static EngineUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private NormalEngine normalEngine;

    public void config(SourceDTO sourceDTO) {
        normalEngine = new NormalEngine(sourceDTO);
    }

    public void getHomeList(int page, OnControllerCallBack callBack) {
        if (normalEngine != null) {
            normalEngine.getHome(page, callBack);
        }
    }
}
