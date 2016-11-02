package com.atguigu.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/31.
 * QQ:443098360
 * 微信：y443098360
 * 作用：适配器
 */
public class NewsDetailPagerAdapter extends PagerAdapter {
    /**
     *新闻详情页面的数据
     */
    private final List<NewsCenterPagerBean2.DataBean.ChildrenBean> childrenData;
    /**
     * 新闻详情页面的UI集合
     */
    private final ArrayList<MenuDetailBasePager> detailBasePagers;

    public NewsDetailPagerAdapter(List<NewsCenterPagerBean2.DataBean.ChildrenBean> childrenData, ArrayList<MenuDetailBasePager> detailBasePagers) {
        this.childrenData = childrenData;
        this.detailBasePagers = detailBasePagers;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return childrenData.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return detailBasePagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MenuDetailBasePager menuDetailBasePager = detailBasePagers.get(position);//TabDetailPager
        View rootView = menuDetailBasePager.rootView;
        menuDetailBasePager.initData();//初始化数据
        container.addView(rootView);
        return rootView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
