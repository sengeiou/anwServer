package com.lz.aiui.adapter;

import android.view.ViewGroup;

import com.lz.aiui.ai.AiItem;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.lz.aiui.adapter
 * 工程名:Anovel
 * 时间:2018/12/26 14:22
 * 说明: 适配器工厂
 */
public interface IAdapterFactory {
    int getItemViewType(int position, AiItem item);

    RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);

    void bindViewHolder(RecyclerView.ViewHolder holder, int position, AiItem item);
}
