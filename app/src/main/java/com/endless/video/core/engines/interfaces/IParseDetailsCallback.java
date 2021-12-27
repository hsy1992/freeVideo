package com.endless.video.core.engines.interfaces;

import com.endless.video.core.bean.vo.AnimeDetailsVO;

/**
 * @ClassName IParseDetailsCallback
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/20 11:18
 * @Version 1.0
 */
public interface IParseDetailsCallback {

    void onResult(AnimeDetailsVO animeDetail);
}
