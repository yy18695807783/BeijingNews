package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 颜银 on 2016/10/13.
 * QQ:443098360
 * 微信：y443098360
 * 作用：缓存工具类-共享偏好
 */
public class CacheUtils {
    /**
     * 缓存文本数据
     * @param context
     * @param key
     * @param result
     */
    public static void putString(Context context, String key, String result) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);//私有化
        sp.edit().putString(key,result).commit();//别忘记提交，保存到内存中
    }

    /**
     * 取出缓存数据 得到文本信息
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");//不要写null  比较会出空指针，用key实际是Url来取出Json数据
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();

    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);//没有值默认返回false
    }
}
