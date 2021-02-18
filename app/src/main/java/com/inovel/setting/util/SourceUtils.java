package com.inovel.setting.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import androidx.annotation.NonNull;

/**
 * 作者:zhouqiang
 * 包名:com.inovel.setting.util
 * 工程名:Anovel
 * 时间:2018/12/27 10:10
 * 说明: 资源的工具类
 */
public class SourceUtils {


    /**
     * 读取assets目录下的文本
     *
     * @param context  上下文
     * @param fileName 文件名[保证文件编码格式utf-8]
     * @return 文件内容
     */
    public static String getFromAssets(@NonNull Context context, @NonNull String fileName) {
        StringBuilder sb = new StringBuilder();
        AssetManager am = context.getAssets();
        InputStream is = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            is = am.open(fileName);
            inputReader = new InputStreamReader(is, Charset.forName("utf-8"));
            bufReader = new BufferedReader(inputReader);

            String line;
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
