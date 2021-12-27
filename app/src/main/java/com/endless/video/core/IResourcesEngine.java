package com.endless.video.core;

import com.endless.video.core.bean.ConfigDTO;
import com.endless.video.core.engines.interfaces.IParseDetailsCallback;
import com.endless.video.core.engines.interfaces.IParseUrlCallback;
import com.endless.video.core.engines.interfaces.ISearchCallback;

/**
 * @ClassName IResources
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/17 13:54
 * @Version 1.0
 */
public interface IResourcesEngine {

    void search(String keyword, ISearchCallback callback);

    void parseDetailsUrl(String detailUrl, IParseDetailsCallback callback);

    void parseRawUrl(String rawUrl, IParseUrlCallback callback);

    String getEngineCode();

    void setEngineInfo(ConfigDTO.AnimeDTO animeDTO);
}
