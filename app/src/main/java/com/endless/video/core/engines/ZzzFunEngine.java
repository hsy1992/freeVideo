package com.endless.video.core.engines;

import com.endless.video.core.CoreConstants;
import com.endless.video.core.engines.interfaces.IParseDetailsCallback;
import com.endless.video.core.engines.interfaces.IParseUrlCallback;
import com.endless.video.core.engines.interfaces.ISearchCallback;

/**
 * @ClassName ZzzFunEngine
 * @Description ZzzFun
 * @Author haosiyuan
 * @Date 2021/12/17 15:11
 * @Version 1.0
 */
public class ZzzFunEngine extends BaseEngine {

    @Override
    public void search(String keyword, ISearchCallback callback) {

    }

    @Override
    public void parseDetailsUrl(String detailUrl, IParseDetailsCallback callback) {

    }

    @Override
    public void parseRawUrl(String rawUrl, IParseUrlCallback callback) {

    }

    @Override
    public String getEngineCode() {
        return CoreConstants.EngineName.ZZZ_FUN;
    }
}
