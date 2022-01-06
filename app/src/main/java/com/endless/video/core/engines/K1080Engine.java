package com.endless.video.core.engines;

import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.bimi.BiMiDetailsTO;
import com.endless.video.core.api.k1080.K1080DetailsTO;
import com.endless.video.core.api.k1080.K1080SearchTO;
import com.endless.video.core.api.k1080.K1080UrlTO;
import com.endless.video.core.api.k4ya.K4YaSearchTO;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName k1080Engine
 * @Description 1080影视
 * @Author haosiyuan
 * @Date 2021/12/17 15:09
 * @Version 1.0
 */
public class K1080Engine  extends BaseEngine {

    private static final String API = "http://myapp.hanmiys.net/";

    @Override
    public void search(String keyword, ISearchCallback callback) {

        EasyHttp.get(this)
                .server(API)
                .api(new K1080SearchTO(keyword))
                .request(new OnHttpListener<JSONObject>() {
                    @Override
                    public void onSucceed(JSONObject jsonObject) {
                        List<AnimeMetaVO> list = new ArrayList<>();
                        try {
                            JSONArray itemList = jsonObject.optJSONObject("data")
                                    .optJSONArray("list");
                            if (itemList != null) {
                                for (int i = 0; i < itemList.length(); i++) {
                                    JSONObject metaObj = itemList.getJSONObject(i);
                                    list.add(new AnimeMetaVO(metaObj.optString("video_name"),
                                            metaObj.optString("cover"),
                                            metaObj.optString("category"),
                                            metaObj.optString("intro"), metaObj.optString("video_id"),
                                            getEngineCode()));

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                .api(new K1080DetailsTO(detailUrl))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        AnimeDetailsVO detail = new AnimeDetailsVO();
                        try {
                            JSONObject data = new JSONObject(result);
                            data = data.getJSONObject("data").getJSONObject("info");
                            detail.setTitle(data.optString("video_name"));
                            detail.setCover_url(data.optString("cover"));
                            detail.setDesc(data.optString("intro").replace("<p>", "").replace("</p>", ""));
                            detail.setCategory(data.optString("category"));

                            JSONArray partsArray = data.getJSONArray("source");
                            List<AnimePlayListVO> animePlayListVOList = new ArrayList<>();
                            for (int i = 0; i < partsArray.length(); i++) {
                                AnimePlayListVO animePlayListVO = new AnimePlayListVO();
                                JSONObject partsObj = partsArray.getJSONObject(i);
                                animePlayListVO.setName(partsObj.getString("name"));
                                List<AnimeVO> list = new ArrayList<>();
                                for (int j = 0; j < partsObj.getJSONArray("videos").length(); j++) {

                                    try {
                                        JSONObject video = partsObj.getJSONArray("videos").optJSONObject(j);
                                        list.add(new AnimeVO(
                                                video.optString("title"),
                                                partsObj.optString("source_id") + '|' +
                                                        partsObj.optString("chapter_id") + '|' +
                                                        partsObj.optString("video_id"),
                                                getEngineCode()
                                        ));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                animePlayListVO.setModule(getEngineCode());
                                animePlayListVO.setAnimeVoList(list);
                                animePlayListVOList.add(animePlayListVO);
                            }

                            detail.setPlaylists(animePlayListVOList);
                            callback.onResult(detail);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onResult(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        callback.onResult(null);
                    }
                });
    }

    @Override
    public void parseRawUrl(String rawUrl, IParseUrlCallback callback) {
        String[] ids = rawUrl.split("|");
        EasyHttp.get(this)
                .server(API)
                .api(new K1080UrlTO(ids[0], ids[1], ids[2]))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        try {
                            JSONObject data = new JSONObject(result);
                            callback.onResult(data.optJSONObject("data").optString("url"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onResult(null);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        callback.onResult(null);
                    }
                });

    }

    @Override
    public String getEngineCode() {
        return CoreConstants.EngineName.K_1080;
    }
}
