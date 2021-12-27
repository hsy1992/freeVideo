package com.endless.video.core.engines;

import static com.scwang.smart.refresh.layout.constant.RefreshState.None;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.StringUtils;
import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.age.AgeDetailsTO;
import com.endless.video.core.api.age.AgeSearchTO;
import com.endless.video.core.api.age.AgeUrlTO;
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
 * @ClassName AgeFansEngine
 * @Description AGE动漫
 * @Author haosiyuan
 * @Date 2021/12/17 15:14
 * @Version 1.0
 */
public class AgeFansEngine extends BaseEngine {

    private final String API = "https://api.agefans.app/";

    @Override
    public void search(String keyword, ISearchCallback callback) {
        EasyHttp.get(this)
                .server(API)
                .api(new AgeSearchTO(keyword, 1))
                .request(new OnHttpListener<JSONObject>() {
                    @Override
                    public void onSucceed(JSONObject jsonObject) {
                        List<AnimeMetaVO> list = new ArrayList<>();
                        try {
                            JSONArray itemList = jsonObject.optJSONArray("AniPreL");
                            if (itemList != null) {
                                for (int i = 0; i < itemList.length(); i++) {
                                    JSONObject metaObj = itemList.getJSONObject(i);
                                    list.add(new AnimeMetaVO(metaObj.optString("R动画名称"),
                                            metaObj.optString("R封面图小"), metaObj.optString("R剧情类型"),
                                            metaObj.optString("R简介"), metaObj.optString("AID"),
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
                .api(new AgeDetailsTO(detailUrl))
                .request(new OnHttpListener<JSONObject>() {
                    @Override
                    public void onSucceed(JSONObject jsonObject) {
                        AnimeDetailsVO animeDetailsVO = new AnimeDetailsVO();
                        try {
                            JSONObject dataObj = jsonObject.optJSONObject("AniInfo");
                            if (dataObj != null) {
                                animeDetailsVO.setTitle(dataObj.optString("R动画名称"));
                                animeDetailsVO.setCover_url("http:" + dataObj.optString("R封面图"));
                                animeDetailsVO.setDesc(dataObj.optString("R简介"));
                                animeDetailsVO.setCategory(dataObj.optString("R标签"));

                                JSONArray playlistJsonArray = dataObj.optJSONArray("R在线播放All");
                                List<AnimePlayListVO> animePlayListVOList = new ArrayList<>();
                                for (int i = 0; i < playlistJsonArray.length(); i++) {
                                    AnimePlayListVO animePlayListVO = new AnimePlayListVO();
                                    animePlayListVO.setName(playlistJsonArray.optJSONObject(0)
                                            .optString("PlayId").replace("<play>", "")
                                            .replace("</play>", "").toUpperCase());
                                    animePlayListVO.setModule(getEngineCode());

                                    List<AnimeVO> animeVOList = new ArrayList<>();
                                    for (int j = 0; j < playlistJsonArray.length(); j++) {
                                        JSONObject playObj = playlistJsonArray.optJSONObject(j);
                                        String play_id = playObj.optString("PlayId");
                                        String play_vid = playObj.optString("PlayVid");
                                        animeVOList.add(new AnimeVO(
                                                StringUtils.isEmpty(playObj.optString("Title_l")) ?
                                                        playObj.optString("Title") : playObj.optString("Title_l"),
                                                play_id + "|" + play_vid, getEngineCode()
                                        ));
                                    }
                                    animePlayListVO.setAnimeVoList(animeVOList);
                                    animePlayListVOList.add(animePlayListVO);
                                }
                                animeDetailsVO.setPlaylists(animePlayListVOList);
                            }
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
                String play_id = params[0];
                String play_vid = params[1];
                if (params[1].startsWith("http")) {
                    callback.onResult(params[1]);
                    return;
                }
                EasyHttp.get(this)
                        .server(API)
                        .api("v2/_getplay")
                        .request(new OnHttpListener<JSONObject>() {
                            @Override
                            public void onSucceed(JSONObject jsonObject) {
                                String url = "";
                                try {
                                    url = jsonObject.getString("Location");
                                    if (StringUtils.isEmpty(url)) {
                                        callback.onResult("");
                                        return;
                                    }

                                    String play_key = "agefans3382-getplay-1719";
                                    String timestamp = jsonObject.getString("ServerTime");
                                    String kp = EncryptUtils.encryptMD5ToString(timestamp + "{|}"
                                            + play_id + "{|}" + play_vid + "{|}" + play_key);
                                    EasyHttp.get(AgeFansEngine.this)
                                            .server(url)
                                            .api(new AgeUrlTO(play_id, play_vid, timestamp, kp))
                                            .request(new OnHttpListener<JSONObject>() {
                                                @Override
                                                public void onSucceed(JSONObject result) {
                                                    String purlf = result.optString("purlf");
                                                    String vurl = result.optString("vurl");
                                                    String newUrl = purlf + vurl;
                                                    if (StringUtils.isEmpty(newUrl)) {
                                                        callback.onResult("");
                                                        return;
                                                    }
                                                    String[] urls = newUrl.split("\\?url=");
                                                    if (urls.length > 0) {
                                                        callback.onResult(urls[urls.length - 1]);
                                                    } else {
                                                        callback.onResult("");
                                                    }
                                                }

                                                @Override
                                                public void onFail(Exception e) {
                                                    callback.onResult("");
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    callback.onResult("");
                                }
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
        return CoreConstants.EngineName.AGE_FANS;
    }
}
