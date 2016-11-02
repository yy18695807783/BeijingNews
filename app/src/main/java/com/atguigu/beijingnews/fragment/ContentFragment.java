package com.atguigu.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.adapter.ContentFragmentAdapter;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsCenterPager;
import com.atguigu.beijingnews.pager.SettingCartPager;
import com.atguigu.beijingnews.pager.ShoppingCartPager;
import com.atguigu.beijingnews.pager.ShoppingPager;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 颜银 on 2016/10/15.
 * QQ:443098360
 * 微信：y443098360
 * 作用：主页面
 */
public class ContentFragment extends BaseFragment {


    //使用xUtils注册
    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewPager;//NoScrollViewPager 继承ViewPager  消费了onTouch事件  不能滑动

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("主页面的视图被初始化了...");
        View view = View.inflate(context, R.layout.fragment_content,null);
//        rg_main = (RadioGroup) view.findViewById(R.id.rg_main);
        //把View注入到xUtils中
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("主业数据被初始化了...");


        basePagers = new ArrayList<>();

        //添加pager
        basePagers.add(new HomePager(context));//主页面
        basePagers.add(new NewsCenterPager(context));//新闻中心页面
        basePagers.add(new ShoppingPager(context));//商城页面
        basePagers.add(new ShoppingCartPager(context));//购物车页面
        basePagers.add(new SettingCartPager(context));//设置中心页面

        //设置适配器
        viewPager.setAdapter(new ContentFragmentAdapter(basePagers));

        //设置RadioGroup的选中状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始对应的页面的数据
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        rg_main.check(R.id.rb_home);//默认选择主页面
        basePagers.get(0).initData();//显示主页面信息

        //设置所有viewPager模式SlidingMenu不可以滑动
        isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

    }

    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         *
         * @param position  Viewpager的位置下角标
         * @param positionOffset   屏幕滑动百分比
         * @param positionOffsetPixels  屏幕滑动像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        /**
         * 当某个页面被选中的时候回调这个方法
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            //调用被选中的页面的initData方法,才会显示此页面信息
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //提取公共的方法
            isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            switch (checkedId) {
                case R.id.rb_home :
                    viewPager.setCurrentItem(0,false);
//                    isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_news :
                    viewPager.setCurrentItem(1,false);
                    isSetTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_shopping :
                    viewPager.setCurrentItem(2,false);
//                    isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_shopping_cart :
                    viewPager.setCurrentItem(3,false);
//                    isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting :
                    viewPager.setCurrentItem(4,false);
//                    isSetTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    /**
     根据传人的参数设置是否让SlidingMenu可以滑动
     */
    private void isSetTouchModeAbove(int touchmodeNone) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeNone);
    }
}
