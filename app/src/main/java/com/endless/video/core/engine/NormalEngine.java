package com.endless.video.core.engine;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.endless.video.core.api.normal.NormalDetailsApi;
import com.endless.video.core.api.normal.NormalHomeApi;
import com.endless.video.core.api.normal.NormalSearchApi;
import com.endless.video.core.bean.NormalXmlData;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;
import com.endless.video.core.bean.mirror.MirrorSerializeVideoInfo;
import com.endless.video.core.interfaces.IMovieController;
import com.endless.video.core.interfaces.OnControllerCallBack;
import com.endless.video.core.util.XmlUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @ClassName NormalEngine
 * @Description 通用引擎源
 * @Author haosiyuan
 * @Date 2022/2/18 15:05
 * @Version 1.0
 */
public class NormalEngine implements IMovieController, LifecycleOwner {

    private SourceDTO sourceDTO;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    public NormalEngine(SourceDTO sourceDTO) {
        this.sourceDTO = sourceDTO;
    }

    @Override
    public SourceDTO getSourceData() {
        return sourceDTO;
    }

    @Override
    public void getHome(int page, OnControllerCallBack callBack) {
        if (ObjectUtils.isEmpty(sourceDTO) || ObjectUtils.isEmpty(sourceDTO.getRoot())) {
            callBack.onHomeBack(null);
            return;
        }
        EasyHttp.get(this)
                .api(new NormalHomeApi("videolist", page))
                .server(createURL())
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        List<MirrorOnceItemSerialize> list = new ArrayList<>();
                        try {
                            ThreadUtils.getIoPool().execute(() -> {
                                JSONObject json;
                                if (!StringUtils.isEmpty(result)) {
                                    //xml 解析json
                                    json = XmlUtil.xml2Json(result);
                                    //xml 转换
                                    NormalXmlData normalXmlData = XmlUtil.json2Data(json);
                                    if (!ObjectUtils.isEmpty(normalXmlData)) {
                                        List<NormalXmlData.VideoData> videoList = normalXmlData.getList().getVideo();
                                        if (CollectionUtils.isNotEmpty(videoList)) {
                                            List<MirrorSerializeVideoInfo> videoInfos = new ArrayList<>();
                                            //解析video url
                                            for (NormalXmlData.VideoData videoData : videoList) {
                                                MirrorSerializeVideoInfo mirrorSerializeVideoInfo = XmlUtil.easyGetVideoURL(videoData.getDl());
                                                if (!ObjectUtils.isEmpty(mirrorSerializeVideoInfo)) {
                                                    videoInfos.add(mirrorSerializeVideoInfo);
                                                }
                                                MirrorOnceItemSerialize mirrorOnceItemSerialize = new MirrorOnceItemSerialize();
                                                mirrorOnceItemSerialize.setId(videoData.getId());
                                                mirrorOnceItemSerialize.setSmallCoverImage(videoData.getPic());
                                                mirrorOnceItemSerialize.setTitle(videoData.getName());
                                                mirrorOnceItemSerialize.setVideos(videoInfos);
                                                if (!StringUtils.isTrimEmpty(videoData.getDes())) {
                                                    mirrorOnceItemSerialize.setDesc(videoData.getDes().replace("<p>", "")
                                                            .replace("</p>", ""));
                                                }
                                                list.add(mirrorOnceItemSerialize);
                                            }
                                        }
                                    }
                                    callBack.onHomeBack(list);
                                } else {
                                    callBack.onHomeBack(null);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onHomeBack(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        callBack.onHomeBack(null);
                    }
                });
    }

    private String createURL() {
        return sourceDTO.getRoot() + sourceDTO.getParseurl();
    }

    @Override
    public void getSearch(String keyword, OnControllerCallBack callBack) {
        if (ObjectUtils.isEmpty(sourceDTO) || ObjectUtils.isEmpty(sourceDTO.getRoot())) {
            callBack.onHomeBack(null);
            return;
        }
        EasyHttp.get(this)
                .api(new NormalSearchApi(1, keyword))
                .server(createURL())
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        List<MirrorOnceItemSerialize> list = new ArrayList<>();
                        try {
                            ThreadUtils.getIoPool().execute(() -> {
                                JSONObject json;
                                if (!StringUtils.isEmpty(result)) {
                                    //xml 解析json
                                    json = XmlUtil.xml2Json(result);
                                    //xml 转换
                                    NormalXmlData normalXmlData = XmlUtil.json2Data(json);
                                    if (!ObjectUtils.isEmpty(normalXmlData)) {

                                        List<NormalXmlData.VideoData> videoList = normalXmlData.getList().getVideo();
                                        if (CollectionUtils.isNotEmpty(videoList)) {
                                            //解析video url
                                            for (NormalXmlData.VideoData videoData : videoList) {
                                                MirrorOnceItemSerialize mirrorOnceItemSerialize = new MirrorOnceItemSerialize();
                                                mirrorOnceItemSerialize.setId(videoData.getId());
                                                mirrorOnceItemSerialize.setSmallCoverImage(sourceDTO.getLogo());
                                                mirrorOnceItemSerialize.setTitle(videoData.getName());
                                                list.add(mirrorOnceItemSerialize);
                                            }
                                        }
                                    }
                                    callBack.onSearchBack(list);
                                } else {
                                    callBack.onSearchBack(null);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onSearchBack(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        callBack.onSearchBack(null);
                    }
                });
    }

    @Override
    public void getDetail(String movieId, OnControllerCallBack callBack) {
        if (ObjectUtils.isEmpty(sourceDTO) || ObjectUtils.isEmpty(sourceDTO.getRoot())) {
            callBack.onHomeBack(null);
            return;
        }
        EasyHttp.get(this)
                .api(new NormalDetailsApi(movieId))
                .server(createURL())
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        final MirrorOnceItemSerialize[] mirrorOnceItemSerialize = {null};
                        try {
                            ThreadUtils.getIoPool().execute(() -> {
                                JSONObject json;
                                if (!StringUtils.isEmpty(result)) {
                                    //xml 解析json
                                    json = XmlUtil.xml2Json(result);
                                    //xml 转换
                                    NormalXmlData normalXmlData = XmlUtil.json2Data(json);
                                    if (!ObjectUtils.isEmpty(normalXmlData)) {

                                        List<NormalXmlData.VideoData> videoList = normalXmlData.getList().getVideo();
                                        if (CollectionUtils.isNotEmpty(videoList)) {
                                            List<MirrorSerializeVideoInfo> videoInfos = new ArrayList<>();

                                            //解析video url
                                            for (NormalXmlData.VideoData videoData : videoList) {
                                                MirrorSerializeVideoInfo mirrorSerializeVideoInfo = XmlUtil.easyGetVideoURL(videoData.getDl());
                                                if (!ObjectUtils.isEmpty(mirrorSerializeVideoInfo)) {
                                                    videoInfos.add(mirrorSerializeVideoInfo);
                                                }
                                                mirrorOnceItemSerialize[0] = new MirrorOnceItemSerialize();
                                                mirrorOnceItemSerialize[0].setId(videoData.getId());
                                                mirrorOnceItemSerialize[0].setSmallCoverImage(videoData.getPic());
                                                mirrorOnceItemSerialize[0].setTitle(videoData.getName());
                                                mirrorOnceItemSerialize[0].setVideos(videoInfos);
                                                mirrorOnceItemSerialize[0].setDesc(videoData.getDes());
                                            }
                                        }
                                    }
                                    callBack.onDetailBack(mirrorOnceItemSerialize[0]);
                                } else {
                                    callBack.onDetailBack(null);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onDetailBack(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        callBack.onHomeBack(null);
                    }
                });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

}
