package com.inovel.setting.global;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

/**
 * Desc: 常用的常量定义
 * <p>
 * Author: zhouqiang
 * PackageName: PACKAGE_NAME
 * ProjectName: Anovel
 * Date: 2019/1/2 11:24
 */
public class Constants {

    /**************************** Common通用型**********************************************************/
    public static final int ABLE = 1;
    public static final int DISABLE = 0;

    /**************************** Intent传递使用**********************************************************/
    public static final String KEY_DATA = "data";
    public static final String ACTION_SELF_XIRI = "com.lzpd.handle.xiri";
    public static final String ACTION_SELF_KEYEVENT = "com.lzpd.handle.keyevent";

    /**************************** CMD 类型**********************************************************/
    public static final String KEY_ACTION_INOVEL = "action_inovel";
    public static final String ACTION_CMD = "cmd";
    public static final String ACTION_HDMI_CMD = "hdmicmd";
    public static final String ACTION_HDMI_HOME_CMD = "hdmihomecmd";
    public static final String ACTION_TO_ACTIVITY = "toActivity";
    public static final String ACTION_TO_SERVICE = "toService";
    public static final String ACTION_IMAGE_CMD = "image_cmd";
    public static final String ACTION_SOURCE = "source";
    public static final String ACTION_STARTUP = "startup";

    /*****************************遥控器发射按键*********************************************************/
    public static final String KEY_EVENT = "keyevent";
    public static final String KEY_POWER = "KEY_POWER";
    public static final String KEY_THREED = "KEY_THREED";
    public static final String KEY_SOURCE = "KEY_SOURCE";
    /*【注意】KEY_SETTINGS字段已对外（系统设置）提供，请谨慎修改该字段及相关功能逻辑*/
    public static final String KEY_SETTINGS = "KEY_SETTINGS";
    public static final String KEY_MENU = "KEY_MENU";
    public static final String KEY_HOME = "KEY_HOME";

    /***************************intent 中methidId 类型***********************************************************/
    public static final String BRIGHTNESS = "brightness";
    public static final String CONTRAST = "contrast";
    public static final String CHROMA = "chroma";
    public static final String TONE = "tone";
    public static final String RESOLUTION = "resolution";
    public static final String KEYSTONE = "keystone";


    /***************************页面类型***********************************************************/
    public static final String VIEW_3D = "3d";
    public static final String VIEW_SIGNAL = "signal";
    public static final String VIEW_SET = "set";

    /***************************信源设置***********************************************************/
    //发送信源ID的key
    public static final String TV_CURRENT_DEVICE_ID = "tv_current_device_id";
    //跳转切换信源的URI
    public static final String COMPONENT_TV_APP = "com.droidlogic.tvsource/com.droidlogic.tvsource.DroidLogicTv";
    //3D状态
    public static final String KEY_3D_STATE = "key_3d_state";

    public static final String CMD_GET_3D_STATE = "#Intent;S.action_inovel=cmd;S.methodId=Get3dState;S.explicit=format;end";

    public static final String CMD_GET_VIDEO_PLAYING = "#Intent;S.action_inovel=cmd;S.methodId=IsVideoPlayerRunning;S.explicit=running;end";

    /***
     * intent 第一模板
     * 单选
     * #Intent;S.action_inovel=cmd;S.methodId=SetProjectInstallMode;S.params=mode;S.state=2;end
     *
     * 获取
     * #Intent;S.action_inovel=cmd;S.methodId=GetFuncSettingState;end
     *
     * 进度条
     * #Intent;S.action_inovel=cmd;S.methodId=SetDLPKeystone;S.params=keystone;end
     *
     * 跳转Activity
     * #Intent;S.action_inovel=toActivity;launchFlags=0x10000000;component=com.lzui.setting/.MainActivity;end
     *
     * #Intent;S.action_inovel=hdmicmd;S.id=6;end
     *
     * #Intent;S.action_inovel=hdmicmd;S.id=7;end
     *
     * TODO 跳转服务
     * ...
     *
     */
    @StringDef({ACTION_CMD, ACTION_HDMI_CMD, ACTION_TO_ACTIVITY, ACTION_TO_SERVICE, ACTION_SOURCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnovelAction {
        String[] ACTIONS = {ACTION_CMD, ACTION_HDMI_CMD, ACTION_TO_ACTIVITY, ACTION_TO_SERVICE, ACTION_SOURCE};
    }



}
