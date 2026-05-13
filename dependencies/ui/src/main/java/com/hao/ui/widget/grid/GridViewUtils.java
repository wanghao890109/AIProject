package com.hao.ui.widget.grid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hao.ui.widget.grid.data.GridPageEntity;
import com.hao.ui.widget.grid.data.GridPageEntity;
import com.hao.ui.widget.grid.adapter.PageSetAdapter;
import com.hao.ui.widget.grid.data.GridPageEntity;
import com.hao.ui.widget.grid.data.GridPageSetEntity;
import com.hao.ui.widget.grid.data.PageEntity;
import com.hao.ui.widget.grid.interfaces.GridClickListener;
import com.hao.ui.widget.grid.interfaces.PageViewInstantiateListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by wanghao 2022/11/28
 */
public class GridViewUtils {

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, GridClickListener emoticonClickListener) {

        if (sCommonPageSetAdapter != null) {
            return sCommonPageSetAdapter;
        }

        PageSetAdapter pageSetAdapter = new PageSetAdapter();

        addEmojiPageSetEntity(pageSetAdapter, context, emoticonClickListener);

        return pageSetAdapter;
    }

    public static void addEmojiPageSetEntity(PageSetAdapter pageSetAdapter, Context context, final GridClickListener emoticonClickListener) {
        ArrayList<String> emojiArray = new ArrayList<>();
        GridPageSetEntity emojiPageSetEntity
                = new GridPageSetEntity.Builder()
                .setLine(0)
                .setRow(7)
                .setEmoticonList(emojiArray)
                .setIPageViewInstantiateItem(new PageViewInstantiateListener(){
                    @Override
                    public View instantiateItem(ViewGroup container, int position, PageEntity pageEntity) {
                        return null;
                    }
                })
                .setShowDelBtn(GridPageEntity.DelBtnStatus.LAST)
                .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }

    public static PageViewInstantiateListener<GridPageEntity> getGridPageViewInstantiateItem(final Class _class, final GridClickListener onEmoticonClickListener) {
        return new PageViewInstantiateListener<GridPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, GridPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    GirdPageView pageView = new GirdPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        BaseAdapter adapter = (BaseAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        pageView.getGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

}
