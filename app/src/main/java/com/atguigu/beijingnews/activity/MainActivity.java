package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String CONTENT_TAG = "content_tag";
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//把标题隐藏
        super.onCreate(savedInstanceState);
        getScreen();

        initView();

        //初始化Fragment
        initFragment();
    }

    private void initView() {
        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.rightmenu);

        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
//        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);//左侧菜单+主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);//左侧菜单+主页

        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页占据的宽度
//        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));
        slidingMenu.setBehindOffset((int) (screenWidth*0.625));
    }


    private void initFragment() {

        //1.开启事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //3.添加主Fragemnt页面
        transaction.replace(R.id.fl_leftmenu,new LeftMenuFragment(), LEFTMENU_TAG);
        transaction.replace(R.id.fl_main_content, new ContentFragment(), CONTENT_TAG);

        //4.提交事务
        transaction.commit();

    }

    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_TAG);
    }

    public void getScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;//屏幕宽度
    }
}

