package com.atguigu.beijingnews.pager.detailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.adapter.NewsDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：新闻详情页面
 */
public class NewsDetailPager extends MenuDetailBasePager {
    /**
     *新闻详情页面的数据
     */
    private final List<NewsCenterPagerBean2.DataBean.ChildrenBean> childrenData;
    /**
     * 新闻详情页面的UI集合
     */
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;

    @ViewInject(R.id.ib_next)
    private ImageButton ib_next;


    public NewsDetailPager(Context context, NewsCenterPagerBean2.DataBean dataBean) {
        super(context);
        childrenData = dataBean.getChildren();
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.news_detail_pager,null);
        x.view().inject(this,view);
        return view;
    }

    @Event(value = R.id.ib_next)
    private void tabNext(View view){
        //有点击事件
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    @Override
    public void initData() {
        super.initData();

        detailBasePagers = new ArrayList<>();
        for (int i = 0;i < childrenData.size();i++){
            //12个页签页面
            TabDetailPager tabDetailPager = new TabDetailPager(context,childrenData.get(i));
            detailBasePagers.add(tabDetailPager);
        }

        //设置适配器了
        viewPager.setAdapter(new NewsDetailPagerAdapter(childrenData,detailBasePagers));

        //TabPageIndicator和ViewPager关联，关联要在ViewPager设置适配器之后
        indicator.setViewPager(viewPager);

        //关联后，监听页面的改变由TabPageIndicator
        indicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if(position== 0){
                //北京第一个页签页面  可以滑动
                setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                //其他 不可以滑动
                setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 主fragment是否可以滑动
     * @param touchmodeNone
     */
    private void setTouchModeAbove(int touchmodeNone) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeNone);
    }
}
