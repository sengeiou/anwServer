package com.inovel.setting.view.holder;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.inovel.setting.ANWApplication;
import com.inovel.setting.R;
import com.inovel.setting.global.Constants;
import com.inovel.setting.util.SharedPreferenceUtil;
import com.inovel.setting.view.holder.helper.IViewHolderOperate;
import com.inovel.setting.view.holder.helper.ViewHolderOperateImpl;
import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.adapter.IViewHolderBinder;
import com.lz.aiui.ai.AiItem;

import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.view.holder
 * 工程名:Anovel
 * 时间:2018/12/26 11:42
 * 说明: 有进度条的ViewHolder
 */
@SuppressWarnings("unchecked")
public class SeekableViewHolder extends RecyclerView.ViewHolder implements IViewHolderBinder<AiItem> {

    private static final String TAG = "SeekableViewHolder";
    /** 基础步进 */
    private static final int STEP = 1;
    /** 长按事件ID */
    private static final int EVENT_LONG_CLICK = 1;
    /** 快速步进时间间隔 */
    private static final int DELAY_LONG_CLICK = 200;
    /** 进度条重新可以使用 */
    private static final int EVENT_ENABLE_SEEKBAR = 2;
    /** 进度条长按后设置值 */
    private static final int EVENT_LONGCLICK_SET_SEEKBAR = 3;

    private long lastClickTime;
    private boolean isEnable = true;
    private boolean hasLongClicked;
    private boolean isLongClick;

    /**
     * 当前进度条的模式 ，
     * 0 代表亮度、对比度、色度、色调，设置范围0-100
     * 1 代表清晰度，设置范围0-30
     * 2 代表白平衡、色彩校正下的颜色设置，设置范围-10~10
     * 3 代表梯形校正设置，设置范围-10~10
     * 4 代表通过TvControlManager 设置的值，亮度，对比度，色饱和度，清晰度都是1-100，
     * 5 代表通过TvControlManager 设置的值 RGB增益为：0~2047，RGB偏移：-1023~1024，但将进度条范围都是 1-100，
     * 6 代表光源设置中的自定义选项：0~20，默认值15，
     */
    private int mCurrentMode;

    public TextView name;
    public SeekBar mSeekBar;
    public TextView flag;
    public SeekBar mUnableSeekBar;

    /** ViewHolder数据操作helper */
    private IViewHolderOperate mHelper;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (mCurrentMode == 3) {
                        LogUtil.d(TAG, "isEnable :" + isEnable);

                        if (isEnable) {
                            int p = updateProgress(mSeekBar.getProgress() - STEP);
                            setupDisplay(false, p);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_ENABLE_SEEKBAR, null), 2000);

                        }
                    } else {
                        updateProgress(mSeekBar.getProgress() - STEP);
                    }
                    break;

                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    if (mCurrentMode == 3) {
                        LogUtil.d(TAG, "isEnable :" + isEnable);
                        if (isEnable) {
                            int p = updateProgress(mSeekBar.getProgress() + STEP);
                            setupDisplay(false, p);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_ENABLE_SEEKBAR, null), 2000);
                        }
                    } else {
//
                        updateProgress(mSeekBar.getProgress() + STEP);
                    }
                    break;

                case EVENT_LONG_CLICK:

                    boolean isPlus = (boolean) msg.obj;
                    int step = isPlus ? getFastStep() : -getFastStep();
                    updateProgress(mSeekBar.getProgress() + step);
                    if (checkProgress((mSeekBar.getProgress() + step)) == 0 ||
                            checkProgress((mSeekBar.getProgress() + step)) == mSeekBar.getMax()) {
                        hasLongClicked = true;
                    }
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_LONG_CLICK, isPlus), DELAY_LONG_CLICK);
                    break;
                case EVENT_ENABLE_SEEKBAR:
                    setupDisplay(true, 0);

                    break;
                case EVENT_LONGCLICK_SET_SEEKBAR:
                    hasLongClicked = true;
                    LogUtil.d(TAG, "EVENT_LONGCLICK_SET_SEEKBAR :hasLongClicked " + hasLongClicked);
                    boolean isPlus1 = (boolean) msg.obj;
                    int step1 = isPlus1 ? getFastStep() : -getFastStep();
                    int p = updateProgress(mSeekBar.getProgress() + step1);
                    setupDisplay(false, p);
                    if (mHandler.hasMessages(EVENT_ENABLE_SEEKBAR)) {
                        mHandler.removeMessages(EVENT_ENABLE_SEEKBAR);
                    }
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_ENABLE_SEEKBAR, null), 2000);
                    break;
                default:
                    return false;
            }
            return true;
        }
    });


    private void setupDisplay(boolean isRevert, int p) {

        if (isRevert) {
            isEnable = isLongClick ? false : true;
            mUnableSeekBar.setVisibility(View.GONE);
            mSeekBar.setVisibility(View.VISIBLE);
            name.setTextColor(ANWApplication.getApplication().getResources().getColor(android.R.color.white));
            flag.setTextColor(ANWApplication.getApplication().getResources().getColor(android.R.color.white));
        } else {
            isEnable = false;
            mUnableSeekBar.setProgress(p);
            mUnableSeekBar.setVisibility(View.VISIBLE);
            mSeekBar.setVisibility(View.GONE);
            name.setTextColor(ANWApplication.getApplication().getResources().getColor(R.color.unable_color));
            flag.setTextColor(ANWApplication.getApplication().getResources().getColor(R.color.unable_color));
        }
    }

    public SeekableViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_menu_name);
        mSeekBar = itemView.findViewById(R.id.sb_seekbar);
        flag = itemView.findViewById(R.id.atv_flag);
        mUnableSeekBar = itemView.findViewById(R.id.sb_seekbar_unable);

        mHelper = new ViewHolderOperateImpl(itemView.getContext()) {
            @Override
            public void reset(@NonNull RecyclerView.ViewHolder holder) {
                //        holder.itemView.setOnKeyListener(null);
                mSeekBar.setOnSeekBarChangeListener(null);//需要重置，不然由于holder缓存导致数据重置
            }
        };
    }


    @Override
    public void bind(RecyclerView.ViewHolder holder, int position, AiItem item) {

        LogUtil.d(TAG, "bind " + position + ", " + item);
        mHelper.reset(holder);

        //初始化数据
        mSeekBar.setVisibility(View.INVISIBLE);
        name.setText(item.getName());
        try {
            initProgress(item);
//            updateProgress(TextUtils.isEmpty(item.getValue()) ? 0 : Integer.parseInt(item.getValue()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            updateProgress(0);
        }

        //初始化监听器
        holder.itemView.setFocusable(true);
        holder.itemView.setClickable(true);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                LogUtil.d(TAG, "onProgressChanged " + progress + ", fromUser " + fromUser + " ,mCurrentMode:" + mCurrentMode);
                int result = transValue(progress);

                LogUtil.d(TAG, "onProgressChanged " + item + ", result :" + result + " ,hasLongClicked: " + hasLongClicked
                        + " ,isEnable:" + isEnable);
                flag.setText(String.valueOf(result));
                //梯形纠正设置后会延时响应，需要做特殊处理
                if (mCurrentMode != 3 || (mCurrentMode == 3 && isEnable) || (mCurrentMode == 3 && hasLongClicked)) {
                    if (!TextUtils.isEmpty(item.getIntent())) {
                        boolean isSuccess = mHelper.dispatchAction(item.getIntent(), result);
                        LogUtil.d(TAG, "dispatchAction result? " + isSuccess);

                    }
//                    isEnable = false;
                    hasLongClicked = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        holder.itemView.setOnClickListener(v -> LzToast.showToast(itemView.getContext(), "" + position, 2000));

        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
//                v.setBackgroundColor(Color.RED);
                mSeekBar.setVisibility(View.VISIBLE);
            } else {
//                v.setBackgroundColor(Color.TRANSPARENT);
                mSeekBar.setVisibility(View.INVISIBLE);
            }
        });

        holder.itemView.setOnKeyListener(new View.OnKeyListener() {
            //处理长按事件
            private long lastTime;


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    //记录第一次按下时间
                    if (event.getRepeatCount() == 0) {
                        lastTime = System.currentTimeMillis();
                    }

                    if (!isLongClick == (System.currentTimeMillis() - lastTime >= 1000)) {

                        isLongClick = true;
                        setupDisplay(true, 0);
                        mHandler.sendMessage(mHandler.obtainMessage(EVENT_LONG_CLICK, keyCode == KeyEvent.KEYCODE_DPAD_RIGHT));
                    }

                    if (!isLongClick) {
//                        isEnable = true;
                        LogUtil.d(TAG, "lastClickTime :" + lastClickTime + " " +
                                (System.currentTimeMillis() - lastClickTime));
                        if (mCurrentMode == 3 && (System.currentTimeMillis() - lastClickTime < 2200)) {
//                            isEnable = false;

                        } else {

                            lastClickTime = System.currentTimeMillis();
                        }
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_LEFT:
                                mHandler.sendEmptyMessage(KeyEvent.KEYCODE_DPAD_LEFT);
                                break;
                            case KeyEvent.KEYCODE_DPAD_RIGHT:
                                mHandler.sendEmptyMessage(KeyEvent.KEYCODE_DPAD_RIGHT);
                                break;

                            default:
                                return false;
                        }
                    }


                    return true;
                } else if (KeyEvent.ACTION_UP == event.getAction()) {
                    LogUtil.d(TAG, "removed EVENT_LONG_CLICK.");
                    if (mCurrentMode == 3 && isLongClick) {
                        mHandler.sendMessage(mHandler.obtainMessage(EVENT_LONGCLICK_SET_SEEKBAR,
                                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT));
                    }
                    isLongClick = false;
                    mHandler.removeMessages(EVENT_LONG_CLICK);
                }

                return false;
            }
        });
    }

    /**
     * 获取快速切换步进
     *
     * @return 快速切换步进
     */
    private int getFastStep() {
        //去总的10%作为快速步进长度
        int fastStep = mSeekBar.getMax() / 10;
        //如果fastStep < STEP, 取2倍STEP
        return fastStep > STEP ? fastStep : STEP * 2;
    }


    /**
     * 根据当前类型将进度条的值转化
     */
    private int transValue(int progress) {

        if (mCurrentMode == 2 || mCurrentMode == 5) {
            return progress - 10;
        } else if (mCurrentMode == 3) {
            return progress - 10;
        } else {
            return progress;
        }

    }

    /**
     * 初始化进度条，根据intent中不同的methodId判断当前类型
     */
    private void initProgress(AiItem item) {
        if (!TextUtils.isEmpty(item.getIntent())) {
            try {
                Intent intentCmd = Intent.parseUri(item.getIntent(), Intent.URI_ALLOW_UNSAFE);
                String action = intentCmd.getStringExtra(Constants.KEY_ACTION_INOVEL);

                if (!TextUtils.isEmpty(action) && Constants.ACTION_CMD.equalsIgnoreCase(action)) {
                    String params = intentCmd.getStringExtra("params");
                    int progress = TextUtils.isEmpty(item.getValue()) ? 0 : Integer.parseInt(item.getValue());
                    flag.setText(String.valueOf(progress));


                    if (!TextUtils.isEmpty(params) && (params.equalsIgnoreCase(Constants.KEYSTONE))) {
                        mSeekBar.setMax(20);
                        mUnableSeekBar.setMax(20);
                        mCurrentMode = 3;
                        progress += 10;

                    } else if (!TextUtils.isEmpty(params) && (params.equalsIgnoreCase("value"))) {
                        mSeekBar.setMax(20);
                        mCurrentMode = 6;
                        progress = SharedPreferenceUtil.getInt("LightSource_user", 15);
                        progress = progress > 5 ? progress - 5 : 0;
                        flag.setText(String.valueOf(progress));

                    } else {
                        mSeekBar.setMax(20);
                        mCurrentMode = 2;
                        progress += 10;
                    }
                    updateProgress(progress);
                    LogUtil.i(TAG, "params:" + params + " ,mCurrentMode :" + mCurrentMode + " progress:" + progress);


                } else if (!TextUtils.isEmpty(action) && Constants.ACTION_IMAGE_CMD.equalsIgnoreCase(action)) {

                    String methodId = intentCmd.getStringExtra("methodId");
                    String params = intentCmd.getStringExtra("params");
                    float result = 0;
                    if (!TextUtils.isEmpty(methodId) && methodId.equalsIgnoreCase("SetWhiteBalance")) {
                        mCurrentMode = 5;
                        mSeekBar.setMax(20);
                        if (!TextUtils.isEmpty(params) && params.contains("gain")) {

                            result = TextUtils.isEmpty(item.getValue()) ? 1024 : Integer.parseInt(item.getValue());

                            result = 20 * result / 2047;

                        } else if (!TextUtils.isEmpty(params) && params.contains("offset")) {

                            result = TextUtils.isEmpty(item.getValue()) ? 0 : Integer.parseInt(item.getValue());
                            result = (result + 1023) < 0 ? 0 : (result + 1023);
                            result = 20 * result / 2047;

                        }
                        result = Math.round(result);
                        updateProgress((int) result);
                        flag.setText((int) result - 10 + "");

                    } else {
                        mCurrentMode = 4;
                        mSeekBar.setMax(100);
                        result = TextUtils.isEmpty(item.getValue()) ? 0 : Integer.parseInt(item.getValue());
                        flag.setText(String.valueOf((int) result));
                        updateProgress((int) result);

                    }
                    LogUtil.i(TAG, "action:" + action + " ,mCurrentMode :" + mCurrentMode + " result:" + result);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新进度
     *
     * @param progress 进度
     */
    private int updateProgress(int progress) {
        int p = checkProgress(progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSeekBar.setProgress(p, true);
        } else {
            mSeekBar.setProgress(p);
        }
        return p;
    }

    /**
     * 校验进度
     *
     * @param progress 传入进度
     * @return 正确阈值
     */
    private int checkProgress(int progress) {
        if (progress < 0) {
            return 0;
        } else if (progress > mSeekBar.getMax()) {
            return mSeekBar.getMax();
        }
        return progress;
    }
}
