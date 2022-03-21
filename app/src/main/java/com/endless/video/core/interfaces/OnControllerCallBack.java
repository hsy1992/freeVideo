package com.endless.video.core.interfaces;

import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;

import java.util.List;

/**
 * @ClassName OnControllerCallBack
 * @Description 数据返回
 * @Author haosiyuan
 * @Date 2022/2/18 14:11
 * @Version 1.0
 */
public interface OnControllerCallBack {

    void onHomeBack(List<MirrorOnceItemSerialize> list);

    void onSearchBack(List<MirrorOnceItemSerialize> list);

    void onDetailBack(MirrorOnceItemSerialize data);

}

