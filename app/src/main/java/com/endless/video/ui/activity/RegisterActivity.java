package com.endless.video.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.endless.video.core.bean.ApiResponse;
import com.endless.video.http.api.AppRegisterApi;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseActivity;
import com.endless.video.R;
import com.endless.video.aop.Log;
import com.endless.video.aop.SingleClick;
import com.endless.video.app.AppActivity;
import com.endless.video.manager.InputTextManager;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.hjq.widget.view.SubmitButton;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 注册界面
 */
public final class RegisterActivity extends AppActivity
        implements TextView.OnEditorActionListener {

    private static final String INTENT_KEY_PHONE = "phone";
    private static final String INTENT_KEY_PASSWORD = "password";

    @Log
    public static void start(BaseActivity activity, String phone, String password, OnRegisterListener listener) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(INTENT_KEY_PHONE, phone);
        intent.putExtra(INTENT_KEY_PASSWORD, password);
        activity.startActivityForResult(intent, (resultCode, data) -> {

            if (listener == null || data == null) {
                return;
            }

            if (resultCode == RESULT_OK) {
                listener.onSucceed(data.getStringExtra(INTENT_KEY_PHONE), data.getStringExtra(INTENT_KEY_PASSWORD));
            } else {
                listener.onCancel();
            }
        });
    }

    private EditText mPhoneView;
    private CountdownView mCountdownView;

    private EditText mCodeView;

    private EditText mFirstPassword;
    private EditText mSecondPassword;

    private SubmitButton mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.register_activity;
    }

    @Override
    protected void initView() {
        mPhoneView = findViewById(R.id.et_register_phone);
        mCountdownView = findViewById(R.id.cv_register_countdown);
        mCodeView = findViewById(R.id.et_register_code);
        mFirstPassword = findViewById(R.id.et_register_password1);
        mSecondPassword = findViewById(R.id.et_register_password2);
        mCommitView = findViewById(R.id.btn_register_commit);

        setOnClickListener(mCountdownView, mCommitView);

        mSecondPassword.setOnEditorActionListener(this);

        // 给这个 View 设置沉浸式，避免状态栏遮挡
        ImmersionBar.setTitleBar(this, findViewById(R.id.tv_register_title));

        InputTextManager.with(this)
                .addView(mPhoneView)
                .addView(mFirstPassword)
                .addView(mSecondPassword)
                .setMain(mCommitView)
                .build();
    }

    @Override
    protected void initData() {
        // 自动填充手机号和密码
        mPhoneView.setText(getString(INTENT_KEY_PHONE));
        mFirstPassword.setText(getString(INTENT_KEY_PASSWORD));
        mSecondPassword.setText(getString(INTENT_KEY_PASSWORD));
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view == mCommitView) {
            if (mPhoneView.getText().toString().length() != 11) {
                mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast(R.string.common_phone_input_error);
                return;
            }

            if (!mFirstPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
                mFirstPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mSecondPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                mCommitView.showError(3000);
                toast(R.string.common_password_input_unlike);
                return;
            }

            // 隐藏软键盘
            hideKeyboard(getCurrentFocus());

//            if (true) {
//                mCommitView.showProgress();
//                postDelayed(() -> {
//                    mCommitView.showSucceed();
//                    postDelayed(() -> {
//                        setResult(RESULT_OK, new Intent()
//                                .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
//                                .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
//                        finish();
//                    }, 1000);
//                }, 2000);
//                return;
//            }

            // 提交注册
            Map<String, Object> map = new HashMap<>();
            map.put("name", mPhoneView.getText().toString());
            map.put("password", mFirstPassword.getText().toString());
            EasyHttp.post(this)
                    .api("user/register")
                    .json(map)
                    .request(new HttpCallback<ApiResponse<String>>(this) {

                        @Override
                        public void onStart(Call call) {
                            mCommitView.showProgress();
                        }

                        @Override
                        public void onEnd(Call call) {}

                        @Override
                        public void onSucceed(ApiResponse<String> response) {
                            if (response.getStatus() == 200) {
                                postDelayed(() -> {
                                    mCommitView.showSucceed();
                                    postDelayed(() -> {
                                        setResult(RESULT_OK, new Intent()
                                                .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
                                                .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
                                        finish();
                                    }, 1000);
                                }, 1000);
                            } else {
                                toast(response.getMessage());
                                postDelayed(() -> {
                                    mCommitView.showError(3000);
                                }, 1000);
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                            postDelayed(() -> {
                                mCommitView.showError(3000);
                            }, 1000);
                        }
                    });
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white)
                // 不要把整个布局顶上去
                .keyboardEnable(true);
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mCommitView.isEnabled()) {
            // 模拟点击注册按钮
            onClick(mCommitView);
            return true;
        }
        return false;
    }

    /**
     * 注册监听
     */
    public interface OnRegisterListener {

        /**
         * 注册成功
         *
         * @param phone             手机号
         * @param password          密码
         */
        void onSucceed(String phone, String password);

        /**
         * 取消注册
         */
        default void onCancel() {}
    }
}