package com.inovel.setting.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inovel.setting.util.PowerKeyUtils;
import com.inovel.setting.util.SharedPreferenceUtil;
import com.lunzn.tool.log.LogUtil;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d("=receive bd: " + action);
        if (action.equals("com.lz.test.romupgrade")) {
        } else if (action.equals("com.lzui.launcher.adv.completed")) {
            intoHdmiHome();
        }
    }

    private void intoHdmiHome() {
//        String pkg = ANWApplication.getSystemControlManager().getProperty("persist.inovel.autorun.pkg");
        String pkg = SharedPreferenceUtil.getString("boot", "home");
        LogUtil.d("pkg: " + pkg);
        if (pkg != null && !pkg.isEmpty()) {
            if (pkg.equals("home")) {
                ;//do nothing
            } else if (pkg.equals("hdmi1")) {
                PowerKeyUtils.checkSource(6);
            } else if (pkg.equals("hdmi2")) {
                PowerKeyUtils.checkSource(7);
            } else {//third part apk
                ;
            }


        }
    }


}
