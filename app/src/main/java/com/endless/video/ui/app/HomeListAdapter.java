package com.endless.video.ui.app;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.endless.video.R;
import com.endless.video.app.AppAdapter;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.core.bean.mirror.MirrorOnceItemSerialize;
import com.endless.video.http.glide.GlideApp;

/**
 * @ClassName HomeListAdapter
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 22:37
 * @Version 1.0
 */
public class HomeListAdapter extends AppAdapter<MirrorOnceItemSerialize> {

    public HomeListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final ImageView ivPic;
        private final TextView tvName;
        private final TextView tvInfo;

        private ViewHolder() {
            super(R.layout.item_home_list);
            ivPic = findViewById(R.id.iv_pic);
            tvName = findViewById(R.id.tv_name);
            tvInfo = findViewById(R.id.tv_info);
        }

        @Override
        public void onBindView(int position) {
            MirrorOnceItemSerialize data = getItem(position);
            tvName.setText(data.getTitle());
            tvInfo.setText(data.getDesc());
            GlideApp.with(getContext())
                    .load(data.getSmallCoverImage())
                    .into(ivPic);
        }
    }
}
