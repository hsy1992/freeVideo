package com.endless.video.core;

import android.content.Context;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MapUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.endless.video.core.bean.ConfigDTO;
import com.endless.video.core.engines.AFangEngine;
import com.endless.video.core.engines.AgeFansEngine;
import com.endless.video.core.engines.BiMiEngine;
import com.endless.video.core.engines.K1080Engine;
import com.endless.video.core.engines.K4YaEngine;
import com.endless.video.core.engines.LibVioEngine;
import com.endless.video.core.engines.YingHuaEngine;
import com.endless.video.core.engines.ZzzFunEngine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName CoreEngine
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/17 13:59
 * @Version 1.0
 */
public class CoreEngine {

    private static class SingletonHolder {
        private static CoreEngine instance = new CoreEngine();
    }

    public static CoreEngine getInstance() {
        return SingletonHolder.instance;
    }

    private Map<String, IResourcesEngine> map = MapUtils.newHashMap();

    private List<IResourcesEngine> currentEngineList = new ArrayList<>(10);

    private boolean init = false;

    public synchronized void init(Context context) {

        if (!init) {
            ThreadUtils.getIoPool().execute(() -> {
                map.put(CoreConstants.EngineName.A_FANG, new AFangEngine());
                map.put(CoreConstants.EngineName.K_1080, new K1080Engine());
                map.put(CoreConstants.EngineName.K_4_YA, new K4YaEngine());
                map.put(CoreConstants.EngineName.LIB_VIO, new LibVioEngine());
                map.put(CoreConstants.EngineName.ZZZ_FUN, new ZzzFunEngine());
                map.put(CoreConstants.EngineName.BI_MI, new BiMiEngine());
                map.put(CoreConstants.EngineName.AGE_FANS, new AgeFansEngine());
                map.put(CoreConstants.EngineName.YING_HUA, new YingHuaEngine());

                currentEngineList.clear();
                List<IResourcesEngine> engineList = getEngines(context);
                if (CollectionUtils.isNotEmpty(engineList)) {
                    currentEngineList.addAll(engineList);
                }
            });
            init = true;
        }
    }

    /**
     * 获取引擎列表
     * @param context
     * @return
     */
    private List<IResourcesEngine> getEngines(Context context) {
        List<IResourcesEngine> engineList = new ArrayList<>();
        try (InputStream is = context.getAssets().open("config.json"); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] bytes = new byte[4 * 1024];
            int len = 0;
            while ((len = is.read(bytes)) != - 1) {
                bos.write(bytes, 0, len);
            }
            final String json = bos.toString();

            final ConfigDTO configDTO = GsonUtils.fromJson(json, ConfigDTO.class);
            if (CollectionUtils.isNotEmpty(configDTO.getAnime())) {
                for (ConfigDTO.AnimeDTO animeDTO : configDTO.getAnime()) {
                    if (animeDTO.isEnable() && ObjectUtils.isNotEmpty(map.get(animeDTO.getModule()))) {
                        Objects.requireNonNull(map.get(animeDTO.getModule())).setEngineInfo(animeDTO);
                        engineList.add(map.get(animeDTO.getModule()));
                    }
                }
            }
            return engineList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return engineList;
    }
}
