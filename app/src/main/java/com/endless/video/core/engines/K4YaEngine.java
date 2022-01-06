package com.endless.video.core.engines;

import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.age.AgeSearchTO;
import com.endless.video.core.api.bimi.BiMiDetailsTO;
import com.endless.video.core.api.k4ya.K4YaDetailsTO;
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
 * @ClassName K4YaEngine
 * @Description 4K鸭奈飞
 * @Author haosiyuan
 * @Date 2021/12/17 15:11
 * @Version 1.0
 */
public class K4YaEngine  extends BaseEngine {

    private static final String API = "http://4kya.net:6969/api.php/";

    @Override
    public void search(String keyword, ISearchCallback callback) {
        EasyHttp.get(this)
                .server(API)
                .api(new K4YaSearchTO(keyword))
                .request(new OnHttpListener<JSONObject>() {
                    @Override
                    public void onSucceed(JSONObject jsonObject) {
                        List<AnimeMetaVO> list = new ArrayList<>();
                        try {
                            JSONArray itemList = jsonObject.optJSONObject("result")
                                    .optJSONArray("rows");
                            if (itemList != null) {
                                for (int i = 0; i < itemList.length(); i++) {
                                    JSONObject metaObj = itemList.getJSONObject(i);
                                    list.add(new AnimeMetaVO(metaObj.optString("title"),
                                            metaObj.optString("pic").replace("mac://", "http://"),
                                            metaObj.optString("ext_types"),
                                            metaObj.optString("blurb"), metaObj.optString("id"),
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
                .api(new K4YaDetailsTO(detailUrl))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        AnimeDetailsVO detail = new AnimeDetailsVO();
                        try {
                            JSONObject data = new JSONObject(result);
                            data = data.getJSONObject("result");
                            detail.setTitle(data.optString("title"));
                            detail.setCover_url(data.optString("pic").replace("mac://", "http://"));
                            detail.setDesc(data.optString("blurb"));
                            detail.setCategory(data.optString("ext_types"));

                            JSONArray partsArray = data.getJSONArray("players");
                            List<AnimePlayListVO> animePlayListVOList = new ArrayList<>();
                            for (int i = 0; i < partsArray.length(); i++) {
                                AnimePlayListVO animePlayListVO = new AnimePlayListVO();
                                JSONObject partsObj = partsArray.getJSONObject(i);
                                animePlayListVO.setName(partsObj.getString("name"));
                                List<AnimeVO> list = new ArrayList<>();
                                for (int j = 0; j < partsObj.getJSONArray("datas").length(); j++) {

                                    try {
                                        JSONObject video = partsObj.getJSONArray("datas").optJSONObject(j);
                                        list.add(new AnimeVO(
                                                video.optString("text"),
                                                        video.optString("play_url"),
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

    }

    @Override
    public String getEngineCode() {
        return CoreConstants.EngineName.K_4_YA;
    }
}
