package com.inovel.setting.view.holder.helper;

import android.content.Intent;
import android.os.Message;

import com.lz.aiui.ai.AiItem;
import com.lz.aiui.interfaces.OnEventTransfer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.view.holder.helper
 * 工程名:Anovel
 * 时间:2018/12/28 10:37
 * 说明:ViewHolder操作接口
 */
public interface IViewHolderOperate<T> {

    /**
     * 重置处理
     * @param holder item
     */
    void reset(@NonNull RecyclerView.ViewHolder holder);

    /**
     * 显示上一页
     */
    void showPre();

    /**
     * 显示下一页
     * @param item 对应数据
     */
    void showNext(T item);

    /**
     * 焦点项发生变化
     * @param position focused位置
     */
    void notifyFocusChanged(int position);

    /**
     * 数据发生变化
     */
    void notifyDataSet();


    boolean dispatchAction(@NonNull String intent,int value);

    /**
     * 新增事件分发接口
     * @param intent {@link AiItem#getAiIntent()}
     * @param eventTransfer 事件回调接口
     * @return 是否分发成功
     */
    boolean dispatchAction(@NonNull Intent intent, OnEventTransfer<Message> eventTransfer);
}
