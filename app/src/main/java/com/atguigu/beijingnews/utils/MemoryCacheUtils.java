package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

/**
 * Created by 颜银 on 2016/10/23.
 * QQ:443098360
 * 微信：y443098360
 * 作用：网络处理三级缓存方法---内存缓存工具
 */
public class MemoryCacheUtils {
    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils() {
        //都除以1024
        int maxSize = (int) ((Runtime.getRuntime().maxMemory())/1024/8);
        lruCache = new LruCache<String, Bitmap>(maxSize){
            //计算每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };
    }

    /**
     * 根据Url添加图片到内存中
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {

        lruCache.put(imageUrl,bitmap);
    }


    /**
     * 根据Url从内存中获取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmap(String imageUrl) {
        return lruCache.get(imageUrl);
    }

}
