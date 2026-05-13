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
import com.hao.ui.R;
import com.hao.ui.base.BaseDialog;

/**
 * 底部弹出的按钮弹窗
 */
public class BottomChoiceDialog extends BaseDialog {

    private RecyclerView recyclerView;
    private TextView tvCancel, tvConfirm;

    private MyAdapter myAdapter = new MyAdapter();
    private OnChoiceClickListener choiceClickListener;

    public BottomChoiceDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_choice;
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
    public void addChoice(int key, String name) {
        BottomChoiceModel model = new BottomChoiceModel(key, name);
        myAdapter.add(model);
    }

    public void initChoice(int key, String name) {
        myAdapter.setChoice(new BottomChoiceModel(key, name));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        recyclerView = mRootView.findViewById(R.id.recycler_view);
        tvCancel = mRootView.findViewById(R.id.tv_cancel);
        tvConfirm = mRootView.findViewById(R.id.tv_confirm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                BottomChoiceModel choiceModel = BottomChoiceDialog.this.myAdapter.getItem(position);
                myAdapter.setChoice(choiceModel);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (myAdapter.getChoiceModel() != null) {
                    choiceClickListener.onChoiceClick(myAdapter.getChoiceModel());
                }
            }
        });
    }

    /**
     * 设置点击按钮监听
     *
     * @param listener
     */
    public void setOnChoiceClickListener(OnChoiceClickListener listener) {
        choiceClickListener = listener;
    }

    /**
     * 菜单模型
     */
    public static class BottomChoiceModel {
        public int key; // 用于标记
        public String name; // 展示的名称

        public BottomChoiceModel(int key, String name) {
            this.key = key;
            this.name = name;
        }
    }

    public interface OnChoiceClickListener {
        void onChoiceClick(BottomChoiceModel model);
    }

    private static class MyAdapter extends BaseQuickAdapter<BottomChoiceModel, QuickViewHolder> {
        private BottomChoiceModel choiceModel;
        public void setChoice(BottomChoiceModel model) {
            choiceModel = model;
            notifyDataSetChanged();
        }

        public BottomChoiceModel getChoiceModel() {
            return choiceModel;
        }

        @NonNull
        @Override
        protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
            return new QuickViewHolder(R.layout.item_bottom_option, parent);
        }

        @Override
        protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, @Nullable BottomChoiceModel item) {
            holder.setText(R.id.item_bottom_option_tv_name, item.name);
            if (choiceModel.key == item.key) {
                holder.setTextColor(R.id.item_bottom_option_tv_name, holder.itemView.getResources().getColor(R.color.purple));
            } else {
                holder.setTextColor(R.id.item_bottom_option_tv_name, holder.itemView.getResources().getColor(R.color.black));
            }
        }
    }

}
