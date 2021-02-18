package com.inovel.setting.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inovel.setting.R;
import com.inovel.setting.view.holder.SeekableViewHolder;
import com.inovel.setting.view.holder.SimpleViewHolder;
import com.lz.aiui.adapter.IAdapterFactory;
import com.lz.aiui.adapter.IViewHolderBinder;
import com.lz.aiui.ai.AiItem;
import com.lz.aiui.ai.AiStyle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.util
 * 工程名:Anovel
 * 时间:2018/12/26 14:32
 * 说明: 适配器工厂实现
 */
@SuppressWarnings("unchecked")
public class AdapterFactoryImpl implements IAdapterFactory {

    /** 布局填充器 */
    private LayoutInflater mInflater;

    public AdapterFactoryImpl(@NonNull Context ctx) {
        this.mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemViewType(int position, AiItem item) {
        int type;
        AiStyle aiStyle = item.getAttrs();
        if (aiStyle == null) { return R.layout.item_common_simple; }
        if (aiStyle.isSeekable()) {
            type = R.layout.item_common_seekable;
        } else if (aiStyle.isMulCheckable()) {
            type = R.layout.item_common_simple;
        } else if (aiStyle.isCheckable()) {
            type = R.layout.item_common_simple;
        } else {
            type = R.layout.item_common_simple;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.item_common_simple:
                return new SimpleViewHolder(v);
            case R.layout.item_common_seekable:
                return new SeekableViewHolder(v);
        }
        throw new RuntimeException("Undefine class of the ViewHolder exception...");
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, int position, AiItem item) {
        if (holder instanceof IViewHolderBinder) {
            ((IViewHolderBinder) holder).bind(holder, position, item);
        } else {
            throw new RuntimeException("Holder must implement IViewHolderBinder...");
        }
    }
}
