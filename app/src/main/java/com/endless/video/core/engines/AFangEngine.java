package com.endless.video.core.engines;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.afang.AFDetailsTO;
import com.endless.video.core.api.afang.AFSearchTO;
import com.endless.video.core.api.afang.AFUrlTO;
import com.endless.video.core.bean.vo.AnimeDetailsVO;
import com.endless.video.core.bean.vo.AnimeMetaVO;
import com.endless.video.core.bean.vo.AnimePlayListVO;
import com.endless.video.core.bean.vo.AnimeVO;
import com.endless.video.core.engines.interfaces.IParseDetailsCallback;
import com.endless.video.core.engines.interfaces.IParseUrlCallback;
import com.endless.video.core.engines.interfaces.ISearchCallback;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AFangEngine
 * @Description 阿房影视
 * @Author haosiyuan
 * @Date 2021/12/17 15:09
 * @Version 1.0
 */
public class AFangEngine extends BaseEngine {

    private static String API = "https://app.bwl87.com/";

    @Override
    public void search(String keyword, ISearchCallback callback) {
        EasyHttp.post(this)
                .server(API)
                .api(new AFSearchTO().setPageSize("12").setPageNum("1").setKeyword(keyword))
                .request(new OnHttpListener<AFSearchTO.Bean>() {
                    @Override
                    public void onSucceed(AFSearchTO.Bean result) {

                        List<AnimeMetaVO> list = new ArrayList<>();
                        if (ObjectUtils.isNotEmpty(result) && ObjectUtils.isNotEmpty(result.getData()) &&
                                CollectionUtils.isNotEmpty(result.getData().getList())) {
                            for (AFSearchTO.Bean.DataBean.ListBean listBean : result.getData().getList()) {
                                if (!StringUtils.isEmpty(listBean.getVideo_id())) {
                                    list.add(new AnimeMetaVO(listBean.getVideo_name(), listBean.getCover(), listBean.getCategory(),
                                            listBean.getIntro(), listBean.getVideo_id(), getEngineCode()));
                                }
                            }
                        }
                        callback.onResult(list);
                    }

                    @Override
                    public void onFail(Exception e) {
                        callback.onResult(new ArrayList<>());
                    }
                });
    }

    @Override
    public void parseDetailsUrl(String detailUrl, IParseDetailsCallback callback) {
        EasyHttp.get(this)
                .server(API)
                .api(new AFDetailsTO().setVideoId(detailUrl))
                .request(new OnHttpListener<JSONObject>() {
                    @Override
                    public void onSucceed(JSONObject jsonObject) {

                        AnimeDetailsVO animeDetailsVO = new AnimeDetailsVO();

                        try {
                            JSONObject info = jsonObject.getJSONObject("data").getJSONObject("info");
                            animeDetailsVO.setTitle(info.optString("video_name"));
                            animeDetailsVO.setCover_url(info.optString("cover"));
                            animeDetailsVO.setDesc(info.optString("intro")
                                    .replace("<p>", "").replace("</p>", ""));
                            animeDetailsVO.setCategory(info.optString("category"));

                            JSONArray videos = info.optJSONArray("videos");
                            JSONArray source = info.optJSONArray("source");

                            List<AnimePlayListVO> playlists = new ArrayList<>();
                            if (ObjectUtils.isNotEmpty(source) &&
                                    ObjectUtils.isNotEmpty(videos)) {

                                for (int i = 0; i < source.length(); i++) {
                                    AnimePlayListVO playListVO = new AnimePlayListVO();
                                    JSONObject sourceObj = source.getJSONObject(i);
                                    playListVO.setName(sourceObj.optString("name"));
                                    playListVO.setModule(getEngineCode());
                                    List<AnimeVO> animeVOList = new ArrayList<>();
                                    for (int j = 0; j < videos.length(); j++) {
                                        JSONObject videoObj = videos.getJSONObject(j);
                                        AnimeVO animeVO = new AnimeVO(videoObj.optString("title"),
                                                String.format("%s|%s|%s", sourceObj.optString("source_id"),
                                                        sourceObj.optString("chapter_id"),
                                                        sourceObj.optString("video_id")), getEngineCode());
                                        animeVOList.add(animeVO);
                                    }
                                    playListVO.setAnimeVoList(animeVOList);
                                    playlists.add(playListVO);
                                }
                            }
                            animeDetailsVO.setPlaylists(playlists);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        callback.onResult(animeDetailsVO);
                    }

                    @Override
                    public void onFail(Exception e) {
                        callback.onResult(new AnimeDetailsVO());
                    }
                });
    }

    @Override
    public void parseRawUrl(String rawUrl, IParseUrlCallback callback) {
        if (!StringUtils.isEmpty(rawUrl)) {
            try {
                String[] params = rawUrl.split("|");
                EasyHttp.get(this)
                        .server(API)
                        .api(new AFUrlTO().setSource_id(params[0]).setChapter_id(params[1]).setVideo_id(params[2]))
                        .request(new OnHttpListener<JSONObject>() {
                            @Override
                            public void onSucceed(JSONObject jsonObject) {
                                String url = "";
                                try {
                                    url = jsonObject.getJSONObject("data").getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callback.onResult(url);
                            }

                            @Override
                            public void onFail(Exception e) {
                                callback.onResult("");
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getEngineCode() {
        return CoreConstants.EngineName.A_FANG;
    }

}
