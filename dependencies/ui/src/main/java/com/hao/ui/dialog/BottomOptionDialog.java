package com.hao.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

import com.hao.ui.base.BaseDialog;
import com.hao.ui.base.BaseDialog;
import com.hao.ui.R;
import com.hao.ui.base.BaseDialog;

/**
 * 底部弹出的按钮弹窗
 */
public class BottomOptionDialog extends BaseDialog {

    private RecyclerView recyclerView;
    private TextView tvCancel;

    private MyAdapter adapter = new MyAdapter();

    public BottomOptionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_option;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    /**
     * 添加一个选项
     *
     * @param key
     * @param name
     */
    public void addOption(int key, String name) {
        BottomOptionModel model = new BottomOptionModel(key, name);
        adapter.add(model);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        recyclerView = mRootView.findViewById(R.id.recycler_view);
        tvCancel = mRootView.findViewById(R.id.tv_cancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 设置点击按钮监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        adapter.setOnItemClickListener(new com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                BottomOptionModel item = BottomOptionDialog.this.adapter.getItem(position);
                listener.onItemClick(item);
            }
        });
    }

    /**
     * 菜单模型
     */
    public static class BottomOptionModel {
        public int key; // 用于标记
        public String name; // 展示的名称

        public BottomOptionModel(int key, String name) {
            this.key = key;
            this.name = name;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BottomOptionModel model);
    }

    private static class MyAdapter extends BaseQuickAdapter<BottomOptionModel, QuickViewHolder> {

        @NonNull
        @Override
        protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
            return new QuickViewHolder(R.layout.item_bottom_option, parent);
        }

        @Override
        protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, @Nullable BottomOptionModel item) {
            holder.setText(R.id.item_bottom_option_tv_name, item.name);
        }
    }

}
