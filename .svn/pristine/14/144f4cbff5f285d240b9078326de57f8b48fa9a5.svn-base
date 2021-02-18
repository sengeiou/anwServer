package com.lz.aiui.ai;

import android.content.Intent;

import com.lunzn.tool.log.LogUtil;

import java.net.URISyntaxException;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * 作者:zhouqiang
 * 包名:com.lz.aiui.ai
 * 工程名:Setting
 * 时间:2018/12/19 14:06
 * 说明: AI bean
 */
public class AiItem implements Comparable<AiItem> {

    public static final String ABLE = "1";
    public static final String ABLE_NO = "0";


    /** 定义优先级，排序之用 */
    @NonNull
    private String priority = "";
    /** 标签名 */
    private String name;
    /** UI样式 */
    private AiStyle attrs;

    /** name对应的数据 */
    private String value;
    /** intent字符串，可以转Intent对象 */
    private String intent;
    /** intent意图对象 */
    private Intent mIntent;

    /** 子菜单 */
    private List<AiItem> subs;

    /** 默认构造，提供json 解析 */
    public AiItem() {
    }

    public AiStyle getAttrs() {
        return attrs;
    }

    public void setAttrs(AiStyle attrs) {
        this.attrs = attrs;
    }

    public void setPriority(@NonNull String priority) {
        this.priority = priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntent(String intent) {
        this.intent = intent;
        try {
            mIntent = Intent.parseUri(intent, Intent.URI_ALLOW_UNSAFE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LogUtil.e("intent =" + intent + ", errorMessage = " + e.getMessage());
        }
    }

    public void setSubs(List<AiItem> subs) {
        this.subs = subs;
    }

    public String getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getIntent() {
        return intent;
    }

    public Intent getAiIntent(){
        return mIntent;
    }

    public List<AiItem> getSubs() {
        return subs;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasSubs() {
        return subs != null && subs.size() > 0;
    }

    @Override
    public String toString() {
        return "AiItem{" +
                "priority='" + priority + '\'' +
                ", name='" + name + '\'' +
                ", attrs=" + attrs +
                ", value='" + value + '\'' +
                ", intent='" + intent + '\'' +
                ", subs=" + subs +
                '}';
    }

    @Override
    public int compareTo(AiItem o) {
        return this.priority.compareTo(o.priority);
    }
}
