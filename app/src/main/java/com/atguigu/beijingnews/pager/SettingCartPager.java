package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BasePager;

import org.xutils.common.util.LogUtil;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：设置中心
 */
public class SettingCartPager extends BasePager {

    public SettingCartPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("设置中心数据被初始化了...");
        //1.设置标题
        tv_title.setText("设置中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("设置中心内容");
    }
}
