package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 颜银 on 2016/10/22.
 * QQ:443098360
 * 微信：y443098360
 * 作用：网络处理三级缓存方法
 */
public class NetCacheUtils {

    /**
     * 请求成功
     */
    public static final int SUCCESS = 1;
    /**
     * 请求失败
     */
    public static final int FAIL = 2;
    /**
     * 发送消息
     */
    private final Handler handler;
    /**
     * 内存缓存工具
     */
    private MemoryCacheUtils memoryCacheUtils;
    /**
     * 本地缓存工具
     */
    private LocaleCacheUtils localeCacheUtils;
    /**
     * 线程池服务
     */
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocaleCacheUtils localeCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler= handler;
        service = Executors.newFixedThreadPool(10);
        this.memoryCacheUtils = memoryCacheUtils;
        this.localeCacheUtils = localeCacheUtils;
    }

    public void getbitmapFromNet(String imageUrl, int position) {
//        new Thread(new MyRunable(imageUrl,position)).start();
        service.execute(new MyRunable(imageUrl,position));

    }

    class MyRunable implements Runnable {

        private final String imageUrl;
        private final int position;

        public MyRunable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            //联网请求图片数据

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//不能小写
                connection.setReadTimeout(4000);
                connection.setConnectTimeout(4000);
                int code = connection.getResponseCode();
                if(code == 200){
                    //联网成功
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    //请求网络图片，获取图片，显示到控件上
                    //向内存存一份
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                    //向本地文件中存一份
                    localeCacheUtils.putBitmap(imageUrl,bitmap);

                    //发消息
                    Message msg = Message.obtain();
                    msg.arg1 = position;
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);


                }else {
                    //联网失败
                }
            } catch (Exception e) {
                e.printStackTrace();
                //异常发送信息
                Message msg = Message.obtain();
                msg.arg1 = position;
                msg.what = FAIL;
                handler.sendMessage(msg);
            }

        }
    }
}
