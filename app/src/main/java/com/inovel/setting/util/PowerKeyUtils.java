package com.inovel.setting.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.inovel.setting.ANWApplication;
import com.inovel.setting.global.Constants;
import com.inovel.setting.view.holder.helper.ViewHolderOperateImpl;
import com.inovel.setting.view.windowdialog.DialogContentLayout;
import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.window.GlobalWindow;


/**
 * Desc: 电源按键工具类
 * <p>
 * Author: zhoulai
 * PackageName: com.inovel.setting.util
 * ProjectName: anwServer
 * Date: 2018/12/13 17:20
 */
public class PowerKeyUtils {

    private static final String TAG = PowerKeyUtils.class.getSimpleName();

    // 关闭屏幕事件
    private static final String SCREEN_OFF_INTENT = "#Intent;S.action_inovel=cmd;S.params=state;S.methodId=SetScreenState;S.state=0;end";

    // 点亮屏幕事件
    private static final String SCREEN_ON_INTENT = "#Intent;S.action_inovel=cmd;S.params=state;S.methodId=SetScreenState;S.state=1;end";

    // 获取屏幕状态
    private static final String SCREEN_STATUS = "#Intent;S.action_inovel=cmd;S.methodId=GetScreenState;S.explicit=state;end";

    //关机Action
    private static final String REQUEST_SHUTDOWN = "android.intent.action.ACTION_REQUEST_SHUTDOWN";

    //关机确认选择
    private static final String KEY_CONFIRM = "android.intent.extra.KEY_CONFIRM";

    // 重启Action
    private static final String REBOOT = "android.intent.action.REBOOT";

    public static final String POWER_VIEW = "PowerView";


    private static PowerKeyUtils instance;
    private final ViewHolderOperateImpl mMHelper;

    private PowerKeyUtils() {
        mMHelper = new ViewHolderOperateImpl(ANWApplication.getApplication());
    }

    public static PowerKeyUtils newInstance() {
        if (null == instance) {
            synchronized (PowerKeyUtils.class) {
                if (null == instance) {
                    instance = new PowerKeyUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 关闭屏幕
     */
    public void screenOff() {
        LogUtil.i(TAG, "screenOff:  ");
        mMHelper.dispatchAction(SCREEN_OFF_INTENT, -1);
    }

    /**
     * 点亮屏幕
     */
    public void screenOn() {
        LogUtil.i(TAG, "screenOn:  ");
        mMHelper.dispatchAction(SCREEN_ON_INTENT, -1);
    }

    /**
     * 获取屏幕状态
     */
    public void getScreenStatus() {
        LogUtil.i(TAG, "getScreenStatus:  ");
        mMHelper.dispatchAction(SCREEN_STATUS, -1);
    }

    /**
     * 关机
     */
    public static void shotDown(Context context) {
        Intent i = new Intent(REQUEST_SHUTDOWN);
        i.putExtra(KEY_CONFIRM, false);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * 重启
     */
    public static void reboot(Context context) {
        Intent i = new Intent(REBOOT);
        // 立即重启：1
        i.putExtra("nowait", 1);
        // 重启次数：1
        i.putExtra("interval", 1);
        // 不出现弹窗：0
        i.putExtra("window", 0);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    /**
     * 显示点击Power键页面
     *
     * @param context 上下文
     * @param selectItem 选中的按钮
     */
    public static void showPowerView(Context context, int selectItem) {
        // 展示Power键布局的View
        View view = GlobalWindow.getInstance().findViewByTag(POWER_VIEW);
        if (view == null || !GlobalWindow.getInstance().isTop(view)) {
            if (GlobalWindow.getInstance().isTop()) {
                GlobalWindow.getInstance().hideAll();
            }
            DialogContentLayout rootView = new DialogContentLayout(context);
            rootView.setSelectItem(selectItem);
            rootView.setTag(POWER_VIEW);
            GlobalWindow.getInstance()
                    .with(context.getApplicationContext())
                    .show(rootView, (v1, isShow) -> LogUtil.d("----------------" + isShow));
        } else {
            if (view instanceof DialogContentLayout) {
                ((DialogContentLayout) view).setSelectItem(selectItem);
            }
        }
    }


    /**
     * 切换信源
     *
     * @param id id=6为hdmi1，id=7为hdmi2
     */
    public static void checkSource(int id) {
        Settings.System.putInt(ANWApplication.getApplication().getContentResolver(), Constants.TV_CURRENT_DEVICE_ID, id);
        Intent intent = new Intent();
        intent.setComponent(ComponentName.unflattenFromString(Constants.COMPONENT_TV_APP));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ANWApplication.getApplication().startActivity(intent);
    }


}

//    /**
//     * 判断屏幕是否为点亮状态
//     *
//     * @return 屏幕是否为点亮状态
//     */
//    public static boolean isScreenOn(Context context) {
//        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
////        return powerManager.isScreenOn();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            return powerManager.isInteractive();
//        } else {
//            return powerManager.isScreenOn();
//        }
//    }

//    /**
//     * 点亮屏幕
//     */
//    public static void screenOn(Context context) {
//        try {
//            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            Class<? extends PowerManager> aClass = powerManager.getClass();
//            Method goToSleep = aClass.getDeclaredMethod("wakeUp", long.class);
//            goToSleep.invoke(powerManager, SystemClock.uptimeMillis());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

