package com.atguigu.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.base.BasePager;

import java.util.ArrayList;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class ContentFragmentAdapter extends PagerAdapter {

    private ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers) {
        this.basePagers = basePagers;
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager basePager = basePagers.get(position);
        View rootView = basePager.rootView;
        container.addView(rootView);
        return rootView;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
