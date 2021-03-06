package com.atguigu.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 颜银 on 2016/10/18.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class HorizontalScrollViewPager extends ViewPager {
    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float startX;
    private float startY;

    /**
     * 1.水平方向滑动
     * a,第0个位置，并且是从左到右滑动
     * getParent().requestDisallowInterceptTouchEvent(false);
     * b,滑动到最后一个，并且是从右到左滑动
     * getParent().requestDisallowInterceptTouchEvent(false);
     * c,其他中间部分
     * getParent().requestDisallowInterceptTouchEvent(true);
     * 2.竖直方向滑动
     * <p/>
     * getParent().requestDisallowInterceptTouchEvent(false);
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //优先要把事件给当前控件
                getParent().requestDisallowInterceptTouchEvent(true);//拦截

                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float endY = ev.getY();
                //计算偏移量
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);

                if (distanceX > distanceY) {
                    //水平方向滑动
                    if (getCurrentItem() == 0 && endX - startX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);//不拦截
                    } else if (getCurrentItem() == getAdapter().getCount() - 1 && endY - startY < 0) {

                        getParent().requestDisallowInterceptTouchEvent(false);//不拦截
                    } else {
                        //其他拦截
                        getParent().requestDisallowInterceptTouchEvent(true);//拦截
                    }
                } else {
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }


                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
