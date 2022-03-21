package com.endless.video.core.util;

import org.dom4j.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.endless.video.core.VideoType;
import com.endless.video.core.bean.NormalXmlData;
import com.endless.video.core.bean.mirror.MirrorSerializeVideoInfo;

import java.util.List;

/**
 * @ClassName XmlUtil
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/2/18 15:53
 * @Version 1.0
 */
public class XmlUtil {

    public static JSONObject xml2Json(String xmlStr) {
        JSONObject jsonObject = new JSONObject();
        try {
            Document doc = DocumentHelper.parseText(xmlStr);
            dom4j2Json(doc.getRootElement(), jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !isEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {//有子元素
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }

            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static NormalXmlData json2Data(JSONObject jsonObject) {
        if (ObjectUtils.isEmpty(jsonObject)) {
            return null;
        }
        return new NormalXmlData(jsonObject);
    }

    public static MirrorSerializeVideoInfo easyGetVideoURL(String raw) {
        if (StringUtils.isEmpty(raw)) return null;
        String[] rawArray = raw.trim().split("\\$");
        String url = "";
        VideoType videoType = VideoType.iframe;
        String name = "";
        for (int i = 0; i < rawArray.length; i++) {
            if (isUrl(rawArray[i])) {
                url = rawArray[i];
                videoType = getVideoType(url);
            } else if (i == 0) {
                name = rawArray[i];
            }
        }
        return new MirrorSerializeVideoInfo(name, videoType, url);
    }

    private static boolean isUrl(String url) {
        if (StringUtils.isEmpty(url) || url.length() > 2083 || url.startsWith("mailto")) return false;
        return url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp");
    }

    /// 简单获取视频链接类型
    public static VideoType getVideoType(String url) {
        if (url.endsWith(".m3u8") || url.endsWith(".m3u")) {
            return VideoType.m3u8;
        } else if (url.endsWith(".mp4")) {
            return VideoType.mp4;
        }
        return VideoType.iframe;
    }
}
