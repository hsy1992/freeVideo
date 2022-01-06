package com.endless.video.core.engines;

import com.blankj.utilcode.util.CollectionUtils;
import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.bimi.BiMiDetailsTO;
import com.endless.video.core.api.k1080.K1080SearchTO;
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
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LibVioEngine
 * @Description LIB在线
 * @Author haosiyuan
 * @Date 2021/12/17 15:11
 * @Version 1.0
 */
public class LibVioEngine extends BaseEngine {

    @Override
    public void search(String keyword, ISearchCallback callback) {
        EasyHttp.get(this)
                .server("https://www.libvio.com/search/-------------.html")
                .api(new K1080SearchTO(keyword))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String responseData) {
                        List<AnimeMetaVO> list = new ArrayList<>();
                        try {
                            JXDocument jxDocument = JXDocument.create(responseData);
                            List<JXNode> listBox = jxDocument.selN("//div[@class=\"stui-vodlist__box\"]");
                            for (int i = 0; i < listBox.size(); i++) {
                                JXNode box = listBox.get(i);
                                list.add(new AnimeMetaVO(box.selOne("a/@title").asString(),
                                        box.selOne("a/@data-original").asString(),
                                        "",
                                        box.selOne("a/span[@class=\"pic-text text-right\"]/text()").asString(),
                                        box.selOne("a/@href").asString(),
                                        getEngineCode()));
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
                .server("https://www.libvio.com" + detailUrl)
                .api(new BiMiDetailsTO(detailUrl))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        AnimeDetailsVO detail = new AnimeDetailsVO();
                        try {
                            JXDocument jxDocument = JXDocument.create(result);
                            JXNode detailInfo = jxDocument.selNOne("//div[@class=\"stui-content__detail\"]");
                            detail.setCover_url(jxDocument.selOne("//a[@class=\"pic\"]/img/@data-original").toString());
                            detail.setTitle(detailInfo.selOne("h1[@class=\"title\"]/text()").asString());
                            detail.setDesc(detailInfo.selOne("//span[@class=\"detail-content\"]/text()").toString());
                            detail.setCategory(detailInfo.selOne("p[1]/text()").toString().split("/")[0]
                                    .replace("类型：", ""));

                            //div[@class="stui-pannel__head clearfix"]/h3/text()");
                            List<Object> playlist_names = jxDocument.sel("//div[@class=\"stui-pannel__head clearfix\"]/h3/text()");
                            List<JXNode> playlists = jxDocument.selN("//ul[@class=\"stui-content__playlist clearfix\"]");

                            List<AnimePlayListVO> animePlayListVOList = new ArrayList<>();
                            for (int i = 0; i < playlists.size(); i++) {
                                AnimePlayListVO animePlayListVO = new AnimePlayListVO();
                                animePlayListVO.setName(playlist_names.get(i).toString().replace(" ", "").replace("\r\n", ""));
                                List<JXNode> data = playlists.get(i).sel(".//li");
                                List<AnimeVO> list = new ArrayList<>();
                                for (int j = 0; j < data.size(); j++) {
                                    String name = data.get(j).selOne("a/text()").asString();
                                    list.add(new AnimeVO(
                                            name,
                                            data.get(j).selOne("a/@href").asString(),
                                            getEngineCode()
                                    ));
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
        return CoreConstants.EngineName.LIB_VIO;
    }
}
