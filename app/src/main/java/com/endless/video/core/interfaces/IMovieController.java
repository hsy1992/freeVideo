package com.endless.video.core.interfaces;

import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;

import java.util.List;

/**
 * @ClassName IMovieController
 * @Description 操作
 * @Author haosiyuan
 * @Date 2022/2/18 11:56
 * @Version 1.0
 */
public interface IMovieController {

    SourceDTO getSourceData();

    void getHome(int page, OnControllerCallBack callBack);

    void getSearch(String keyword, OnControllerCallBack callBack);

    void getDetail(String movieId, OnControllerCallBack callBack);
}
