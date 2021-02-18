package com.lz.aiui.adapter;

import android.view.ViewGroup;

import com.lz.aiui.ai.AiItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.view.adapter
 * 工程名:Anovel
 * 时间:2018/12/25 17:06
 * 说明: 普通列表适配器
 */
public class CommonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = "CommonRecyclerAdapter";
    /** 数据 */
    private List<AiItem> mList;

    private IAdapterFactory mAdapterFactory;

    private CommonRecyclerAdapter(IAdapterFactory adapterFactory, List<AiItem> list) {
        this.mList = list;
        this.mAdapterFactory = adapterFactory;
    }

    public CommonRecyclerAdapter(IAdapterFactory adapterFactory) {
        this(adapterFactory, new ArrayList<AiItem>());
    }


    @Override
    public int getItemViewType(int position) {
        return mAdapterFactory.getItemViewType(position, getItem(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mAdapterFactory.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mAdapterFactory.bindViewHolder(holder, position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    public AiItem getItem(int position) {
        return mList.get(position);
    }

    public List<AiItem> getList() {
        return mList;
    }
}
