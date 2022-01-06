package com.endless.video.core.engines;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ViewUtils;
import com.endless.video.R;
import com.endless.video.app.AppApplication;
import com.endless.video.core.CoreConstants;
import com.endless.video.core.api.bimi.BiMiDetailsTO;
import com.endless.video.core.api.bimi.BiMiSearchTO;
import com.endless.video.core.api.bimi.BiMiUrlTO;
import com.endless.video.core.bean.vo.AnimeDetailsVO;
import com.endless.video.core.bean.vo.AnimeMetaVO;
import com.endless.video.core.bean.vo.AnimePlayListVO;
import com.endless.video.core.bean.vo.AnimeVO;
import com.endless.video.core.engines.interfaces.IParseDetailsCallback;
import com.endless.video.core.engines.interfaces.IParseUrlCallback;
import com.endless.video.core.engines.interfaces.ISearchCallback;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.http.model.ResponseClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName BiMiEngine
 * @Description 哔咪动漫
 * @Author haosiyuan
 * @Date 2021/12/17 15:14
 * @Version 1.0
 */
public class BiMiEngine extends BaseEngine {

    private static final String API = "http://api.tianbo17.com/";

    @Override
    public void search(String keyword, ISearchCallback callback) {
        EasyHttp.get(this)
                .server(API)
                .api(new BiMiSearchTO("100", keyword, "1"))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        List<AnimeMetaVO> list = new ArrayList<>();
                        try {
                            JSONObject data = new JSONObject(decryptResponse(result));
                            JSONObject dataObj = data.getJSONObject("data");
                            int total = dataObj.getInt("total");
                            if (total > 0) {
                                JSONArray itemsJsonArray = dataObj.getJSONArray("items");
                                for (int i = 0; i < itemsJsonArray.length(); i++) {
                                    JSONObject itemObj = itemsJsonArray.getJSONObject(i);
                                    list.add(new AnimeMetaVO(
                                            itemObj.getString("name"),
                                            itemObj.getString("pic"),
                                            itemObj.getString("type"),
                                            "",
                                            itemObj.getString("id"),
                                            getEngineCode()
                                    ));
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
                .api(new BiMiDetailsTO(detailUrl))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        AnimeDetailsVO detail = new AnimeDetailsVO();
                        try {
                            JSONObject data = new JSONObject(decryptResponse(result));
                            data = data.getJSONObject("data");
                            detail.setTitle(data.optString("name"));
                            detail.setCover_url(data.optString("pic"));
                            detail.setDesc(data.optString("content"));
                            detail.setCategory(data.optString("type"));
                            JSONArray partsArray = data.getJSONArray("parts");
                            List<AnimePlayListVO> animePlayListVOList = new ArrayList<>();
                            for (int i = 0; i < partsArray.length(); i++) {
                                AnimePlayListVO animePlayListVO = new AnimePlayListVO();
                                JSONObject partsObj = partsArray.getJSONObject(i);
                                animePlayListVO.setName(partsObj.getString("play_zh"));
                                List<AnimeVO> list = new ArrayList<>();
                                for (int j = 0; j < partsObj.getJSONArray("part").length(); j++) {
                                    String name = partsObj.getJSONArray("part").getString(j);
                                    list.add(new AnimeVO(
                                            name,
                                            String.format("?id=%s&play=%s&part=%s",
                                                    detailUrl, partsObj.getString("'play'"), name),
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
        EasyHttp.get(this)
                .server(API)
                .api(new BiMiUrlTO(rawUrl))
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {
                        String url = "";
                        try {
                            JSONObject data = new JSONObject(decryptResponse(result));
                            url = data.getJSONArray("data").getJSONObject(0)
                                    .getString("url");
                            if (data.getJSONArray("data").getJSONObject(0).has("parse")) {
                                //不是直链
                                String parse_js = data.getJSONArray("data").getJSONObject(0)
                                        .getString("parse");
                                List<String> parse_apis = findAll(parse_js);
                                String finalUrl = url;
                                ThreadUtils.getIoPool().execute(() -> {
                                    String real_url = "";
                                    for (int i = 0; i < parse_apis.size(); i++) {
                                        try {
                                            real_url = EasyHttp.get(BiMiEngine.this).server(parse_apis.get(i) + finalUrl).
                                                    execute(new ResponseClass<JSONObject>() {
                                                    }).optString("url");
                                            if (!StringUtils.isEmpty(real_url)) {
                                                break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    String finalReal_url = real_url;
                                    ViewUtils.runOnUiThread(() -> {
                                        callback.onResult(finalReal_url);
                                    });
                                });
                            } else {
                                callback.onResult(url);
                            }

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

    }

    @Override
    public String getEngineCode() {
        return CoreConstants.EngineName.BI_MI;
    }

    private String decryptResponse(String data) {
        String split0 = data.split(".")[0];
        String split1 = data.split(".")[1];
        String private_key = AppApplication.instance.getString(R.string.bimi_key)
                .replace("\n", "").replace(" ", "");

        byte[] aes_key = rsaDecrypt(split0, private_key);
        byte[] iv = new byte[16];
        int index = 15;
        for (int i = aes_key.length - 2; i > aes_key.length - 18; i++) {
            iv[index] = aes_key[i];
            index--;
        }

        return aesDecrypt(split1, aes_key, iv);
    }

    private byte[] rsaDecrypt(String encryptedText, String privateKey) {
        byte[] key = EncodeUtils.base64Encode(privateKey);
        return EncryptUtils.decryptBase64RSA(EncodeUtils.base64Encode(encryptedText), key, key.length,
                "RSA/ECB/PKCS1Padding");
    }

    private String aesDecrypt(String encryptedText, byte[] aesKey, byte[] iv) {
        String text = EncryptUtils.encryptAES2HexString(EncodeUtils.base64Encode(encryptedText), aesKey,
                "AES/CBC/PKCS7Padding", iv);
        return text;
    }

    private List<String> findAll(String text) {
        Matcher m = Pattern.compile("(https?://.+?)").matcher(text);
        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }
}
