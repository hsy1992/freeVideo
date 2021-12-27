package com.endless.video.core.engines.interfaces;

import com.endless.video.core.bean.vo.AnimeMetaVO;

import java.util.List;

/**
 * @ClassName ISearchCallBack
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/18 21:59
 * @Version 1.0
 */
public interface ISearchCallback {

    void onResult(List<AnimeMetaVO> metaVoList);
}
