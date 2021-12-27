package com.endless.video.core.engines;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.endless.video.core.IResourcesEngine;
import com.endless.video.core.bean.ConfigDTO;

/**
 * @ClassName BaseEngine
 * @Description TODO
 * @Author haosiyuan
 * @Date 2021/12/17 17:00
 * @Version 1.0
 */
public abstract class BaseEngine implements IResourcesEngine, LifecycleOwner {

    protected ConfigDTO.AnimeDTO engineInfo;

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);


    @Override
    public void setEngineInfo(ConfigDTO.AnimeDTO engineInfo) {
        this.engineInfo = engineInfo;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }
}
