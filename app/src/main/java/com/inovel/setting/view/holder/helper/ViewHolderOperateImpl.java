package com.inovel.setting.view.holder.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;

import com.inovel.setting.ANWApplication;
import com.inovel.setting.global.Constants;
import com.inovel.setting.util.CMDManager;
import com.inovel.setting.util.SharedPreferenceUtil;
import com.inovel.setting.view.windowview.CommMenuView;
import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.ai.AiItem;
import com.lz.aiui.interfaces.OnEventTransfer;
import com.lz.aiui.window.GlobalWindow;

import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.view.holder.helper
 * 工程名:Anovel
 * 时间:2018/12/28 10:40
 * 说明:
 */
public class ViewHolderOperateImpl implements IViewHolderOperate<AiItem> {


    private static final String TAG = "ViewHolderOperateImpl";

    private Context mContext;

    public ViewHolderOperateImpl(Context context) {
        mContext = context;
    }

    @Override
    public void reset(@NonNull RecyclerView.ViewHolder holder) {

    }

    @Override
    public void showPre() {
        sendMessage(CommMenuView.EVENT_PRE, null);
    }

    @Override
    public void showNext(AiItem item) {
        boolean hasNext = item.getSubs() != null && item.getSubs().size() > 0;
        LogUtil.d(TAG, hasNext ? "showNext " : "showNext No subs " + item.getName());
        if (hasNext) {
            sendMessage(CommMenuView.EVENT_NEXT, item);
        }
    }

    @Override
    public void notifyFocusChanged(int position) {
        sendMessage(CommMenuView.EVENT_FOCUS_CHANGED, position);
    }

    @Override
    public void notifyDataSet() {
        sendMessage(CommMenuView.EVENT_VALUE_SET, null);
    }

    @Override
    public boolean dispatchAction(@NonNull String intent, int value) {
        LogUtil.d(TAG, "intent = " + intent);

        try {
            Intent it = Intent.parseUri(intent, Intent.URI_ALLOW_UNSAFE);
            it.putExtra(Constants.KEY_DATA, value);
            return dispatchAction(it, null);
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dispatchAction(@NonNull Intent intent, OnEventTransfer<Message> eventTransfer) {
        try {
            String action = intent.getStringExtra(Constants.KEY_ACTION_INOVEL);

            if (TextUtils.isEmpty(action)) {
                LogUtil.e("The intent that defined actions must have a(n) " + Constants.KEY_ACTION_INOVEL + " string.");
                return false;
            } else {
                assert action != null;
                switch (action) {
                    case Constants.ACTION_CMD:
                        CMDManager.getInstance().sendCMD(intent.toUri(0), intent.getIntExtra(Constants.KEY_DATA, -1));
                        break;
                    case Constants.ACTION_HDMI_CMD:
                        CMDManager.getInstance().sendCMD(intent.toUri(0), intent.getIntExtra(Constants.KEY_DATA, -1));
                        break;
                    case Constants.ACTION_HDMI_HOME_CMD:
                        CMDManager.getInstance().sendCMD(intent.toUri(0), intent.getIntExtra(Constants.KEY_DATA, -1));
                        break;
                    case Constants.ACTION_TO_ACTIVITY:
                        mContext.startActivity(intent);
                        sendMessage(CommMenuView.CANCEL_DIALOG, null);
                        break;
                    case Constants.ACTION_TO_SERVICE:
                        mContext.startService(intent);
                        break;
                    case Constants.ACTION_IMAGE_CMD:
                        //图像模式通过TvControlManager类 发送命令
                        CMDManager.getInstance().sendImageCMD(intent.toUri(0), intent.getIntExtra(Constants.KEY_DATA, -1));
                        break;
                    case Constants.ACTION_SOURCE:
                        try {
                            int state = Settings.System.getInt(ANWApplication.getApplication().getContentResolver(), Constants.TV_CURRENT_DEVICE_ID);
                            CMDManager.getInstance().sendMsg(2, state, null);
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constants.ACTION_STARTUP:
//                        String pkg = ANWApplication.getSystemControlManager().getProperty("persist.inovel.autorun.pkg");
                        String pkg = SharedPreferenceUtil.getString("boot", "home");
                        LogUtil.i(TAG, "ACTION_STARTUP :" + pkg);
                        if (pkg != null && !pkg.isEmpty()) {
                            if (pkg.equals("home")) {
                                CMDManager.getInstance().sendMsg(4, 0, null);
                            } else if (pkg.equals("hdmi1")) {
                                CMDManager.getInstance().sendMsg(4, 1, null);
                            } else if (pkg.equals("hdmi2")) {
                                CMDManager.getInstance().sendMsg(4, 2, null);
                            } else {
                                CMDManager.getInstance().sendMsg(4, 0, null);
                            }
                        } else {

                            CMDManager.getInstance().sendMsg(4, 0, null);
                        }
                        break;
                    default:
                        LogUtil.e(TAG, "does not support intent...");
                        return false;
                }
                return true;
            }

        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    private <T> void sendMessage(int what, T item) {
        Message message = new Message();
        message.what = what;
        message.obj = item;
        GlobalWindow.getInstance().updateViews(message);
    }
}
