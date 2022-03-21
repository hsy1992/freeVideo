package com.endless.video.ui.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.endless.video.R;
import com.endless.video.app.AppFragment;
import com.endless.video.app.TitleBarFragment;
import com.endless.video.constants.Constants;
import com.endless.video.core.bean.ApiResponse;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;
import com.endless.video.core.engine.EngineUtil;
import com.endless.video.core.engine.NormalEngine;
import com.endless.video.core.interfaces.OnControllerCallBack;
import com.endless.video.ui.activity.HomeActivity;
import com.endless.video.ui.adapter.TabAdapter;
import com.endless.video.ui.fragment.BrowserFragment;
import com.endless.video.ui.fragment.StatusFragment;
import com.endless.video.utl.ResultCodeUtil;
import com.endless.video.widget.XCollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseActivity;
import com.hjq.base.FragmentPagerAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 首页 Fragment
 */
public final class HomeFragment extends TitleBarFragment<HomeActivity>
        implements TabAdapter.OnTabListener, ViewPager.OnPageChangeListener,
        XCollapsingToolbarLayout.OnScrimsListener {

    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    private TextView tvSource;
    private TextView mHintView;
    private AppCompatImageView mSearchView;

    private RecyclerView mTabView;
    private ViewPager mViewPager;

    private TabAdapter mTabAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;
    private SourceDTO sourceDTO;
    private HomeListFragment homeListFragment = HomeListFragment.newInstance();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_app;
    }

    @Override
    protected void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.ctl_home_bar);
        mToolbar = findViewById(R.id.tb_home_title);

        tvSource = findViewById(R.id.tv_source);
        mHintView = findViewById(R.id.tv_home_hint);
        mSearchView = findViewById(R.id.iv_home_search);

        mTabView = findViewById(R.id.rv_home_tab);
        mViewPager = findViewById(R.id.vp_home_pager);

        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(homeListFragment, "推荐列表");
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mTabAdapter = new TabAdapter(getAttachActivity());
        mTabView.setAdapter(mTabAdapter);

        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        tvSource.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SourceListActivity.class);
            intent.putExtra("source", sourceDTO);
            startActivityForResult(intent, (resultCode, data) -> {
                if (data != null) {
                    sourceDTO = (SourceDTO) data.getSerializableExtra("source");
                    tvSource.setText(sourceDTO.getName());
                    homeListFragment.refresh(sourceDTO);
                }
            });
        });
    }

    @Override
    protected void initData() {
        mTabAdapter.addItem("推荐列表");
        mTabAdapter.setOnTabListener(this);
        String source = SPUtils.getInstance().getString(Constants.SPKey.SOURCE);
        if (TextUtils.isEmpty(source)) {
            initSourceList();
        } else {
            sourceDTO = JSON.parseObject(source, SourceDTO.class);
            tvSource.setText(sourceDTO.getName());
            EngineUtil.getInstance().config(sourceDTO);
        }
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
                            sourceDTO = response.getData().get(0);
                            EngineUtil.getInstance().config(sourceDTO);
                            SPUtils.getInstance().put(Constants.SPKey.SOURCE, JSON.toJSONString(sourceDTO));
                            SPUtils.getInstance().put(Constants.SPKey.SOURCE_LIST, JSON.toJSONString(response.getData()));
                            tvSource.setText(sourceDTO.getName());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        toast(e.getMessage());
                    }
                });
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    /**
     * {@link TabAdapter.OnTabListener}
     */

    @Override
    public boolean onTabSelected(RecyclerView recyclerView, int position) {
        mViewPager.setCurrentItem(position);
        return true;
    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (mTabAdapter == null) {
            return;
        }
        mTabAdapter.setSelectedPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * CollapsingToolbarLayout 渐变回调
     * <p>
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        getStatusBarConfig().statusBarDarkFont(shown).init();
        tvSource.setTextColor(ContextCompat.getColor(getAttachActivity(), shown ? R.color.black : R.color.black));
        mHintView.setBackgroundResource(shown ? R.drawable.home_search_bar_gray_bg : R.drawable.home_search_bar_gray_bg);
        mHintView.setTextColor(ContextCompat.getColor(getAttachActivity(), shown ? R.color.black60 : R.color.black60));
        mSearchView.setSupportImageTintList(ColorStateList.valueOf(getColor(shown ? R.color.common_icon_color : R.color.common_icon_color)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.setAdapter(null);
        mViewPager.removeOnPageChangeListener(this);
        mTabAdapter.setOnTabListener(null);
    }
}