package com.inovel.setting.view.holder;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.inovel.setting.ANWApplication;
import com.inovel.setting.R;
import com.inovel.setting.global.Constants;
import com.inovel.setting.service.XiriCommandService;
import com.inovel.setting.view.holder.helper.IViewHolderOperate;
import com.inovel.setting.view.holder.helper.ViewHolderOperateImpl;
import com.lunzn.tool.log.LogUtil;
import com.lz.aiui.adapter.IViewHolderBinder;
import com.lz.aiui.ai.AiItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.view.holder
 * 工程名:Anovel
 * 时间:2018/12/25 18:22
 * 说明:
 */
@SuppressWarnings("unchecked")
public class SimpleViewHolder extends RecyclerView.ViewHolder implements IViewHolderBinder<AiItem> {

    private static final String TAG = "SimpleViewHolder";

    public TextView name;
    private CheckedTextView flag;

    /** ViewHolder数据操作helper */
    private IViewHolderOperate mHelper;

    /** 是否可以操作 */
    private boolean isCanUse;

    private Handler mHandler;

    public SimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_menu_name);
        flag = itemView.findViewById(R.id.ctv_flag);
        mHandler = new Handler();

        mHelper = new ViewHolderOperateImpl(itemView.getContext()) {
            @Override
            public void reset(@NonNull RecyclerView.ViewHolder holder) {
                flag.setVisibility(View.VISIBLE);
                name.setTextColor(ANWApplication.getApplication().getResources().getColor(android.R.color.white));
            }
        };

    }


    @Override
    public void bind(RecyclerView.ViewHolder holder, int position, AiItem item) {

        mHelper.reset(holder);
        name.setText(item.getName());

        if (item.getAttrs().isCheckable()) {
            flag.setBackgroundResource(R.drawable.checker_ox);
            flag.setChecked(AiItem.ABLE.equals(item.getValue()));
        } else {
            if (item.hasSubs() || !TextUtils.isEmpty(item.getAttrs().getIconType())) {
                flag.setBackgroundResource(R.drawable.ic_arrow_right);
            } else {
                flag.setVisibility(View.INVISIBLE);
            }
        }

        isCanUse = true;
        holder.itemView.setFocusable(true);
        holder.itemView.setClickable(true);

        LogUtil.d(TAG, "name " + item.getName());
        if (item.getName().equalsIgnoreCase("上下格式") || item.getName().equalsIgnoreCase("左右格式")
                || item.getName().equalsIgnoreCase("蓝光3D") || item.getName().equalsIgnoreCase("关闭3D")) {

            LogUtil.d(TAG, "name " + item.getName() + ", " + item.getAttrs().getMulCheckable());

            if (item.getAttrs().getMulCheckable().equalsIgnoreCase(AiItem.ABLE_NO)) {
                name.setTextColor(ANWApplication.getApplication().getResources().getColor(R.color.unable_color));
                isCanUse = false;

            }
        }
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
//            LogUtil.d(TAG, "focusChanged " + hasFocus + ", position = " + holder.getLayoutPosition());
            if (hasFocus) {
                mHelper.notifyFocusChanged(holder.getLayoutPosition());
            }
        });

        holder.itemView.setOnKeyListener((v, keyCode, event) -> {
            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        mHelper.showPre();
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        LogUtil.d(TAG, "KEYCODE_DPAD_RIGHT isCanUse " + isCanUse);
                        if (isCanUse) {

                            if (item.getAttrs().isCheckable() && !flag.isChecked()) {
                                flag.toggle();
                                mHelper.notifyDataSet();

                            }
                            goNext(item);
                        }
                        break;

                    default:
                        return false;
                }
                return true;
            }

            return false;
        });

        holder.itemView.setOnClickListener(v -> {
            LogUtil.d(TAG, "setOnClickListener isCanUse " + isCanUse);
            if (isCanUse) {
                if (item.getAttrs().isCheckable() && !flag.isChecked()) {
                    flag.toggle();
                    mHelper.notifyDataSet();

                }
                goNext(item);
            }

        });
    }

    /**
     * 跳转到下一级
     * 两条命令一起法，会造成只返回一条数据
     * @param item item数据
     */
    private void goNext(AiItem item) {
        String intent = item.getIntent();
        LogUtil.d(TAG, "intent : " + intent);
        boolean isSuccess = mHelper.dispatchAction(intent, -1);
        if (item.getName().equalsIgnoreCase(XiriCommandService.SET3D)) {
            LogUtil.d(TAG, "---------多发一条获取信息---------------------");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHelper.dispatchAction(Constants.CMD_GET_VIDEO_PLAYING, -1);
                }
            }, 100);
        }
        LogUtil.d(TAG, "dispatchAction result? " + isSuccess);

        //如果有下一级，进入下一级,延时是因为需要获取状态
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.showNext(item);
            }
        }, 100);
    }


}
