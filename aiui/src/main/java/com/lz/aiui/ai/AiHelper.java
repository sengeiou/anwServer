package com.lz.aiui.ai;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lz.aiui.interfaces.IFlatten;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 作者:zhouqiang
 * 包名:com.lz.aiui.ai
 * 工程名:Setting
 * 时间:2018/12/19 15:22
 * 说明: AIUI辅助类
 * 调用:AiHelper<AiItem> helper = new AiHelper<AiItem>(){}
 */
public class AiHelper<T> implements IFlatten<T, String> {

    protected final Class<?> mParamClz;

    private IFlatten<T, String> mParser;

    protected AiHelper() {
        mParamClz = getActualType0();
        mParser = new AiFlatten();
    }

    private Class<?> getActualType0() {
        Class c = this.getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            return (Class<?>) p[0];
        }
        return null;
    }

    public IFlatten getParser() {
        return mParser;
    }

    @Override
    public T flatten(String target) {
        return mParser.flatten(target);
    }


    public class AiFlatten implements IFlatten<T, String> {

        @Override
        public T flatten(String json) throws JSONException {
            return (T) JSONObject.parseObject(json, mParamClz == null ? JSONObject.class : mParamClz);
        }
    }
}
