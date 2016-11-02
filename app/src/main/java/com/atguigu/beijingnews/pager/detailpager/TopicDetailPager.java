package com.atguigu.beijingnews.pager.detailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：主题详情页面
 */
public class TopicDetailPager extends MenuDetailBasePager {


    /**
     *主题详情页面的数据
     */
    private final List<NewsCenterPagerBean2.DataBean.ChildrenBean> childrenData;

    /**
     * 主题详情页面的UI集合
     */
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

//    @ViewInject(R.id.indicator)
//    private TabPageIndicator indicator;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.ib_next)
    private ImageButton ib_next;


    public TopicDetailPager(Context context, NewsCenterPagerBean2.DataBean dataBean) {
        super(context);
        childrenData = dataBean.getChildren();
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.topic_detail_pager,null);
        x.view().inject(this,view);
        return view;
    }

    @Event(value = R.id.ib_next)
    private void tabNext(View view){
        //有点击事件
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void initData() {
        super.initData();

        detailBasePagers = new ArrayList<>();
        for (int i = 0;i < childrenData.size();i++){
            //12个页签页面
            TopicTabDetailPager tabDetailPager = new TopicTabDetailPager(context,childrenData.get(i));
            detailBasePagers.add(tabDetailPager);
        }

        //设置适配器了
        NewsDetailPagerAdapter adapter = new NewsDetailPagerAdapter();
        viewPager.setAdapter(adapter);
        //注意了setupWithViewPager必须在ViewPager.setAdapter()之后调用
        tabLayout.setupWithViewPager(viewPager);


//        //TabPageIndicator和ViewPager关联，关联要在ViewPager设置适配器之后
//        indicator.setViewPager(viewPager);
//
//        //关联后，监听页面的改变由TabPageIndicator
//        indicator.setOnPageChangeListener(new MyOnPageChangeListener());


        //注意以后监听页面的变化 ，TabPageIndicator监听页面的变化 改为 viewPager监听页面的变化
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置可以滚动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        //给标题设置图片（动画小圆点）
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(getTabView(i));
//        }

    }

    /**
     * 给tabLayout设置动画的方法 -- 此处设置点  可以替换
     * @param position
     * @return
     */
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(childrenData.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if(position== 0){
                //北京  可以滑动
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
     *
     * @param touchmodeNone
     */
    private void setTouchModeAbove(int touchmodeNone) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeNone);
    }

    class NewsDetailPagerAdapter extends PagerAdapter {

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
}
