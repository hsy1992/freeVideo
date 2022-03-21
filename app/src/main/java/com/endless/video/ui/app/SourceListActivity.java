package com.endless.video.ui.app;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.endless.video.R;
import com.endless.video.app.AppActivity;
import com.endless.video.constants.Constants;
import com.endless.video.core.bean.ApiResponse;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.ui.popup.ListPopup;
import com.endless.video.utl.ResultCodeUtil;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

/**
 * @ClassName SourceListActivity
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 13:15
 * @Version 1.0
 */
public class SourceListActivity extends AppActivity {

    private WrapRecyclerView rvSource;
    private SourceDTO sourceDTO;
    private SourceListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.source_list_activity;
    }

    @Override
    protected void initView() {
        rvSource = findViewById(R.id.rv_source);
        rvSource.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SourceListAdapter(getContext());
        adapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            for (int i = 0; i < adapter.getData().size(); i++) {
                adapter.getData().get(i).setSelect(position == i);
            }
            sourceDTO = adapter.getData().get(position);
            adapter.notifyDataSetChanged();
        });
        rvSource.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        sourceDTO = getSerializable("source");
        initSourceList();
    }

    @Override
    public void onRightClick(View view) {
        //保存
        SPUtils.getInstance().put(Constants.SPKey.SOURCE, JSON.toJSONString(sourceDTO));
        Intent intent = new Intent();
        intent.putExtra("source", sourceDTO);
        setResult(1000, intent);
        finish();
    }

    private void initSourceList() {
        EasyHttp.post(this)
                .api("user/getSourceList")
                .json(new HashMap<>())
                .request(new HttpCallback<ApiResponse<List<SourceDTO>>>(this) {

                    @Override
                    public void onStart(Call call) {
                        showDialog();
                    }

                    @Override
                    public void onEnd(Call call) {
                        hideDialog();
                    }

                    @Override
                    public void onSucceed(ApiResponse<List<SourceDTO>> response) {
                        if (ResultCodeUtil.checkCode(response.getStatus())) {
                            SPUtils.getInstance().put(Constants.SPKey.SOURCE_LIST, JSON.toJSONString(response.getData()));
                            if (sourceDTO == null) {
                                response.getData().get(0).setSelect(true);
                            } else {
                                for (int i = 0; i < response.getData().size(); i++) {
                                    if (Objects.equals(sourceDTO.getsId(), response.getData().get(i).getsId())) {
                                        response.getData().get(i).setSelect(true);
                                    }
                                }
                            }
                            adapter.setData(response.getData());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        toast(e.getMessage());
                    }
                });
    }
}
