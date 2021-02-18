package com.inovel.setting.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * @author 钟何亮
 * @version [版本号]
 * @date 2017年1月17日
 * @project COM.LZ.M02.LAUNCHER
 * @package com.lz.m02.launcher.util
 * @filename SharedPreferenceUtil.java
 * @since 关于SharedPreference的操作方法
 */
public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences = null;

    private static Editor mEditor = null;

    /**
     * 使用这个工具类之前必须在Application里面调用这个初始化方法
     *
     * @param context 上下文
     * @return 返回工具类的实例
     */
    public static void init(Context context) {
        init(context, "users");
    }

    /**
     * 使用这个工具类之前必须在Application里面调用这个初始化方法
     *
     * @param context 上下文
     * @param name    名称
     */
    public static void init(Context context, String name) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    /**
     * 往users.xml里面保存 int 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, int value) {
        mEditor = sharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    /**
     * 往users.xml里面保存 long 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, long value) {
        mEditor = sharedPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    /**
     * 往users.xml里面保存 float 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, float value) {
        mEditor = sharedPreferences.edit();
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    /**
     * 往users.xml里面保存 boolean 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, boolean value) {
        mEditor = sharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    /**
     * 往users.xml里面保存 String 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, String value) {
        mEditor = sharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    /**
     * 往users.xml里面保存 Set<String> 数据
     *
     * @param key   键值对名称
     * @param value 保存的值
     */
    public static void set(String key, Set<String> value) {
        mEditor = sharedPreferences.edit();
        mEditor.putStringSet(key, value);
        mEditor.apply();
    }

    /**
     * 从users.xml里面获取 字段为 key 的 int 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 int 数据
     */
    public static int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    /**
     * 从users.xml里面获取 字段为 key 的 long 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 long 数据
     */
    public static long getLong(String key, long def) {
        return sharedPreferences.getLong(key, def);
    }

    /**
     * 从users.xml里面获取 字段为 key 的 float 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 float 数据
     */
    public static float getFloat(String key, float def) {
        return sharedPreferences.getFloat(key, def);
    }

    /**
     * 从users.xml里面获取 字段为 key 的 boolean 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 boolean 数据
     */
    public static boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    /**
     * 从users.xml里面获取 字段为 key 的 String 数据，默认返回值为 ""
     *
     * @param key 键值对名称
     * @return key对应的 String 数据
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * 从users.xml里面获取 字段为 key 的 String 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 String 数据
     */
    public static String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    /**
     * 从users.xml里面获取 字段为 key 的 Set<String> 数据
     *
     * @param key 键值对名称
     * @param def 默认返回的值
     * @return key对应的 Set<String> 数据
     */
    public static Set<String> getStringSet(String key, Set<String> def) {
        return sharedPreferences.getStringSet(key, def);
    }

    /**
     * 从users.xml里面删除字段为 keys 数据
     *
     * @param keys
     */
    public static void removeAllKeys(String[] keys) {
        mEditor = sharedPreferences.edit();
        for (int i = 0, l = keys.length; i < l; i++) {
            mEditor.remove(keys[i]);
        }
        mEditor.apply();
    }


    /**
     * 从users.xml里面删除字段为 key 数据
     *
     * @param key 删除保存的键值
     */
    public static void removeKey(String key) {
        mEditor = sharedPreferences.edit();
        mEditor.remove(key);
        mEditor.apply();
    }


    /**
     * 清除所有数据
     */
    public static void clear() {
        mEditor = sharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }
}
