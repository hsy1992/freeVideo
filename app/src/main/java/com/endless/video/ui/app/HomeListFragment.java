package com.endless.video.ui.app;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.endless.video.R;
import com.endless.video.app.AppActivity;
import com.endless.video.app.TitleBarFragment;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;
import com.endless.video.core.engine.EngineUtil;
import com.endless.video.core.interfaces.OnControllerCallBack;
import com.endless.video.ui.adapter.StatusAdapter;
import com.hjq.base.BaseAdapter;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2020/07/10
 * desc   : 加载案例 Fragment
 */
public final class HomeListFragment extends TitleBarFragment<AppActivity>
        implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener {

    private int page = 1;

    public static HomeListFragment newInstance() {
        return new HomeListFragment();
    }

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    private HomeListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.status_fragment;
    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.rl_status_refresh);
        mRecyclerView = findViewById(R.id.rv_status_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mAdapter = new HomeListAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);


        mRefreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        getHomeList(true);
    }

    /**
     * 模拟数据
     */
    private List<String> analogData() {
        List<String> data = new ArrayList<>();
        for (int i = mAdapter.getCount(); i < mAdapter.getCount() + 20; i++) {
            data.add("我是第" + i + "条目");
        }
        return data;
    }

    /**
     * {@link BaseAdapter.OnItemClickListener}
     *
     * @param recyclerView RecyclerView对象
     * @param itemView     被点击的条目对象
     * @param position     被点击的条目位置
     */
    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        toast(mAdapter.getItem(position));
    }

    /**
     * {@link OnRefreshLoadMoreListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        postDelayed(() -> {
            mAdapter.clearData();
            getHomeList(true);
        }, 1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        postDelayed(() -> {
            page++;
            getHomeList(false);
        }, 1000);
    }

    public void getHomeList(boolean isRefresh) {
        EngineUtil.getInstance().getHomeList(page, new OnControllerCallBack() {
            @Override
            public void onHomeBack(List<MirrorOnceItemSerialize> list) {
                ThreadUtils.runOnUiThread(() -> {
                    if (isRefresh) {
                        mAdapter.setData(list);
                        mRefreshLayout.finishRefresh();
                    } else {
                        mAdapter.addData(list);
                        mRefreshLayout.finishLoadMore();
                        mAdapter.setLastPage(mAdapter.getCount() >= 200);
                        mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
                    }
                });

            }

            @Override
            public void onSearchBack(List<MirrorOnceItemSerialize> list) {
            }

            @Override
            public void onDetailBack(MirrorOnceItemSerialize data) {
            }
        });
    }

    public void refresh(SourceDTO sourceDTO) {
        //刷新列表
        EngineUtil.getInstance().config(sourceDTO);
        page = 1;
        postDelayed(() -> {
            mAdapter.clearData();
            getHomeList(true);
        }, 1000);

    }
}