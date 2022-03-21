package com.endless.video.ui.app;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.endless.video.R;
import com.endless.video.app.AppAdapter;
import com.endless.video.core.bean.SourceDTO;
import com.endless.video.http.glide.GlideApp;

/**
 * @ClassName SourceListAdapter
 * @Description TODO
 * @Author haosiyuan
 * @Date 2022/3/20 13:42
 * @Version 1.0
 */
public class SourceListAdapter extends AppAdapter<SourceDTO> {

    public SourceListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView mTvName;
        private final ImageView ivLogo;
        private final AppCompatCheckBox tvSelectCheckbox;

        private ViewHolder() {
            super(R.layout.source_list_item);
            mTvName = findViewById(R.id.tv_name);
            ivLogo = findViewById(R.id.iv_logo);
            tvSelectCheckbox = findViewById(R.id.tv_select_checkbox);
        }

        @Override
        public void onBindView(int position) {
            SourceDTO data = getItem(position);
            mTvName.setText(data.getName());
            GlideApp.with(getContext())
                    .load(data.getLogo())
                    .into(ivLogo);
            tvSelectCheckbox.setChecked(data.isSelect());
            tvSelectCheckbox.setEnabled(false);
        }
    }
}
