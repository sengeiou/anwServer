package com.inovel.setting.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.inovel.setting.global.Constants;
import com.inovel.setting.service.ANWKeyService;
import com.inovel.setting.view.windowview.CommMenuView;
import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.window.GlobalWindow;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler = new Handler(msg -> {
        switch (msg.what){
            case 1:

                break;
        }
            return false;
    });

    private Thread mThread =  new Thread(new Runnable() {
        @Override
        public void run() {
            mThread.run();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(this, MenuActivity.class));
//        Intent intent =new Intent(this,ANWKeyService.class);
//        intent.putExtra(ANWKeyService.KEY_EVENT,ANWKeyService.KEY_THREED);
//        startService(intent);

        Intent intent = new Intent(this, ANWKeyService.class);
        intent.setPackage("com.inovel.setting");
        intent.putExtra(Constants.KEY_EVENT,Constants.KEY_MENU);
        startService(intent);
        finish();


//        ANWKeyService.mCurrentType = 0;
//        forTest();
//        Intent intent2 = new Intent(Constants.ACTION_TO_ACTIVITY);
//        intent2.setComponent(new ComponentName("com.lzui.setting", "com.lzui.setting.MainActivity"));
//        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent2.putExtra("methodId", "methodId");
//        intent2.putExtra("state", "stateId");
//        intent.setAction("action.11111111");
//        LogUtil.i(TAG, intent2.toUri(0));
//        //#Intent;S.methodId=methodId;S.state=stateId;end


//        try {
//            Intent intent1 = Intent.parseUri("#Intent;S.methodId=methodId;S.state=stateId;end", Intent.URI_ALLOW_UNSAFE);
//            String methodId = intent1.getStringExtra("methodId");
//            String action = intent1.getAction();
//            LogUtil.i(TAG, "methodId = " + methodId);
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    public void forTest() {
        //for test
        View v = new CommMenuView(getApplicationContext());
        GlobalWindow.getInstance()
                .with(getApplicationContext())
                .show(v, (v1, isShow) -> LogUtil.d("----------------" + isShow));
    }

}
