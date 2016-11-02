package com.atguigu.beijingnews;

import android.app.Application;
import android.content.Context;

import com.atguigu.beijingnews.volley.VolleyManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 颜银 on 2016/10/16.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class BeijingNewApplaction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //xUtils初始化
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //volley新的初始化
        VolleyManager.init(this);

        //激光推送初始化
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        //ImageLoader初始化
        initImageLoader(this);


        //android监听软件异常崩溃 的初始化
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());


    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }



}
