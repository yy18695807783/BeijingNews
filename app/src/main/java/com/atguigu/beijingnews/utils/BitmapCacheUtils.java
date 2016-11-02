package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by 颜银 on 2016/10/22.
 * QQ:443098360
 * 微信：y443098360
 * 作用：三级缓存工具类
 */
public class BitmapCacheUtils {

    /**
     * 内存缓存
     */
    private MemoryCacheUtils memoryCacheUtils;
    /**
     * 本地缓存
     */
    private LocaleCacheUtils localeCacheUtils;
    /**
     * 网络存储
     */
    private NetCacheUtils netCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localeCacheUtils = new LocaleCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler,localeCacheUtils,memoryCacheUtils);
    }

    /**
     * 三级缓存设计步骤：
     *   * 从内存中取图片
     *   * 从本地文件中取图片
     *        向内存中保持一份
     *   * 请求网络图片，获取图片，显示到控件上
     *      * 向内存存一份
     *      * 向本地文件中存一份
     *
     * @param imageUrl
     * @param position
     * @return
     */
    public Bitmap getBitmap(String imageUrl, int position) {
        //从内存中取图片
        if(memoryCacheUtils!=null){
            Bitmap bitmap =  memoryCacheUtils.getBitmap(imageUrl);
            if (bitmap != null) {
                //从内存获取的图片
                LogUtil.e("从内存获取的图片=="+position);
                return  bitmap;
            }
        }
        //从本地文件中取图片
        if(localeCacheUtils!=null){
            Bitmap bitmap =  localeCacheUtils.getBitmap(imageUrl);
            if (bitmap != null) {
                //从本地获取的图片
                LogUtil.e("从内存获取的图片=="+position);
                return  bitmap;
            }
        }
        //请求网络图片，获取图片，显示到控件上
        if(netCacheUtils!=null){
            netCacheUtils.getbitmapFromNet(imageUrl,position);
        }

        return null;
    }
}
