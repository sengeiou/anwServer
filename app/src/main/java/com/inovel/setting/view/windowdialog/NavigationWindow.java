package com.inovel.setting.view.windowdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.lunzn.tool.log.LogUtil;

/**
 * Desc: 弹窗基类
 * <p>
 * Author: zhoulai
 * PackageName: com.inovel.setting.view.windowdialog
 * ProjectName: anwServer
 * Date: 2018/12/26 9:26
 */
public abstract class NavigationWindow<T> implements WindowsStateCallback {

    //====================变量常量区======================
    private static final String TAG = NavigationWindow.class.getSimpleName();

    protected Context mContext;

    // 布局加载器
    protected LayoutInflater mInflater;


    //========================View区=======================
    protected DialogContentLayout mRootView;

    //====================WindowManager相关==================
    // WindowManager视图
    protected WindowManager mWindow;

    // 内容区域，用于展示动画
    protected View mContentView;

    // view宽度
    protected int mViewWidth;

    // 布局是否显示
    protected boolean isShow;

    // 动画是否在执行
    protected boolean isAnimal;

    //当前页面的数据
    protected T mBean;


    public NavigationWindow(Context context) {
        this.mContext = context;
        this.mWindow = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 获取布局参数
     */
    public abstract WindowManager.LayoutParams getLayoutParam();


    /**
     * 是否正在显示
     */
    public boolean isShowing() {
        return isShow;
    }


    /**
     * 创建视图
     */
    protected abstract void createRootView(Context mContext, T data);


    @Override
    public void windowsEndAnimation(boolean isShow) {
    }


    /**
     * 显示对话框
     *
     * @param data 需要填充得数据
     */
    protected void show(T data) {
        mBean = data;
        if (isShow) {
            close();
        }
        if (!isShow) {
            isShow = true;
            createRootView(mContext, data);
            WindowManager.LayoutParams layoutParam = getLayoutParam();
            layoutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
            mWindow.addView(mRootView, layoutParam);
            startAnimal(true);
        }
    }


    /**
     * 开始执行动画
     *
     * @param isShow 显示还是隐藏  true 显示  false 隐藏
     */
    private void startAnimal(boolean isShow) {
        Animation localTranslateAnimation = getAnimation(isShow);
        if (null != localTranslateAnimation && mContentView != null) {
            localTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation paramAnimation) {
                    LogUtil.i(TAG, "onAnimationEnd");
                    isAnimal = false;
                    if (!isShow) {
                        dismiss();
                    }
                }

                public void onAnimationRepeat(Animation paramAnimation) {
                    LogUtil.i(TAG, "onAnimationRepeat");
                }

                public void onAnimationStart(Animation paramAnimation) {
                    LogUtil.i(TAG, "onAnimationStart");
                    isAnimal = true;
                }
            });
            mContentView.startAnimation(localTranslateAnimation);
        }
    }

    /**
     * 获取执行动画
     *
     * @return 执行的动画
     */
    protected abstract Animation getAnimation(boolean isShow);

    /**
     * 执行关闭操作
     */
    protected void close() {
        if (isShow) {
            this.isShow = false;
            startAnimal(false);
        }
    }

    /**
     * 隐藏控制栏
     */
    private void dismiss() {
        if (mRootView != null) {
            mWindow.removeView(mRootView);
        }
    }
}
