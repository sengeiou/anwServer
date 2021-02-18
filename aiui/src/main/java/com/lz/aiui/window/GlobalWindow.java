package com.lz.aiui.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.interfaces.OnEventTransfer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 作者:zhouqiang
 * 包名:com.lz.aiui.window
 * 工程名:Setting
 * 时间:2018/12/19 15:00
 * 说明:
 * GlobalWindow.getInstance().with(context)
 */
public class GlobalWindow {

    public static final String TAG = "GlobalWindow";
    /** 默认参数 */
    private LayoutParams mWmParams;
    /** Just WindowManager实例 */
    private WindowManager mWindowManager;
    /** window管理集合Set */
    private HashSet<View> mAttachedViews;
    /** window回调接口集合Map */
    private Map<View, OnWindowVisibleChangeListener> mOnWindowVisibleChangeListenerMap;

    private GlobalWindow() {
        mAttachedViews = new HashSet<>();
        mOnWindowVisibleChangeListenerMap = new HashMap<>();
    }

    private static final class SingleInstance {
        static final GlobalWindow INSTANCE = new GlobalWindow();
    }

    public static GlobalWindow getInstance() {
        return SingleInstance.INSTANCE;
    }

    /**
     * 初始化GlobalWindow
     *
     * @param context  上下文
     * @param wmParams LayoutParams
     * @return GlobalWindow
     */
    public GlobalWindow with(@NonNull Context context, @Nullable LayoutParams wmParams) {
        // if未初始化
        if (mWmParams == null || mWindowManager == null) {
            //获取的是WindowManagerImpl.CompatModeWrapper
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            mWmParams = wmParams == null ? getDefaultParams() : wmParams;
        }
        return SingleInstance.INSTANCE;
    }


    public GlobalWindow with(@NonNull Context context) {
        return with(context, getDefaultParams());
    }

    public void show(@NonNull View v, OnWindowVisibleChangeListener l) {
        show(v, mWmParams, l);
    }

    /**
     * 刷新视图
     *
     * @param message Message
     */
    @SuppressWarnings("unchecked")
    public <T> void updateViews(@NonNull T message) {
        for (View view : mAttachedViews) {
            updateView(view, message);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void updateView(@NonNull View view, @NonNull T message){
        if (view instanceof OnEventTransfer) {
            ((OnEventTransfer) view).onEvent(message);
        }
    }


    /**
     * 显示window
     *
     * @param v      view
     * @param params LayoutParams
     * @param l      回调监听
     */
    public void show(@NonNull View v, LayoutParams params, OnWindowVisibleChangeListener l) {
        if (!isShow(v)) {
            mWindowManager.addView(v, params);
            mAttachedViews.add(v);
            addOnWindowVisibleChangeListener(v, l);
            if (l != null) {
                l.onShow(v, true);
            }
        }
    }


    /**
     * 隐藏所有window
     */
    @SuppressWarnings("all")
    public void hide(@NonNull View... views) {
        View view;
        for (int i = 0; i < views.length; i++) {
            view = views[i];
            LogUtil.i(TAG, "view " + view);
            if (mAttachedViews.contains(view)) {

                mWindowManager.removeViewImmediate(view);
                mAttachedViews.remove(view);
                OnWindowVisibleChangeListener l = mOnWindowVisibleChangeListenerMap.remove(view);
                if (l != null) {
                    l.onShow(view, false);
                }
            }
        }
    }

    /**
     * 通过View的tag隐藏
     *
     * @param tag View的tag
     */
    public void hideByTag(@NonNull Object tag) {
        for (View v : mAttachedViews) {
            if (v == null) { continue; }
            if (Objects.equals(v.getTag(), tag)) {
                hide(v);
            }
        }
    }


    public void hideAll() {
        hide(mAttachedViews.toArray(new View[0]));
    }

    /**
     * 根据tag找view
     *
     * @param tag tag
     * @return view
     */
    public View findViewByTag(@NonNull Object tag) {
        for (View v : mAttachedViews) {
            if (v == null) { continue; }
            if (Objects.equals(v.getTag(), tag)) {
                return v;
            }
        }
        return null;
    }


    /**
     * 通过class查找View
     * @param clz class
     * @param <T> 类型
     * @return 返回对应View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewByClass(Class<? extends View> clz){
        for (View v : mAttachedViews) {
            if (v == null) { continue; }
            if (v.getClass() == clz) {
                return (T) v;
            }
        }
        return null;
    }


    /**
     * @param v target
     * @return 是否显示
     */
    public boolean isShow(@NonNull View v) {
        return mAttachedViews.contains(v) && v.getVisibility() == View.VISIBLE;
    }

    /**
     * @return 是否在同类型窗口顶层
     */
    public boolean isTop() {
        return !mAttachedViews.isEmpty();
    }

    /**
     * @return 是否在同类型窗口顶层
     */
    public boolean isTop(@NonNull View v) {
        return mAttachedViews.contains(v);
    }

    /**
     * window显示默认参数
     *
     * @return LayoutParams
     */
    public LayoutParams getDefaultParams() {
        LayoutParams params = new LayoutParams();
        //设置window type
        params.type = LayoutParams.TYPE_PHONE;
        //设置浮动窗口可聚焦
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM | LayoutParams.FLAG_DIM_BEHIND;
        //调整悬浮窗显示的停靠位置为左侧置顶
        params.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
//		mWmParams.x = 0;
//		mWmParams.y = 0;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        //设置透明度（这是窗体本身的透明度，非背景）
        params.alpha = 1f;
        //设置背景模糊 结合LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0f;
        //设置悬浮窗口长宽数据
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        return params;
    }


    public void addOnWindowVisibleChangeListener(View v, OnWindowVisibleChangeListener mOnWindowVisibleChangeListener) {
        mOnWindowVisibleChangeListenerMap.put(v, mOnWindowVisibleChangeListener);
    }

    /**
     * 全局弹框显示状态回调
     */
    public interface OnWindowVisibleChangeListener {
        /**
         * window显示、隐藏回调
         *
         * @param v      对应view
         * @param isShow 是否显示
         */
        void onShow(View v, boolean isShow);
    }

}
