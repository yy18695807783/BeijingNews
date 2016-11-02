package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：新闻页面的基类
 */
public class BasePager {

    public Context context;
    /**
     * 显示标题
     */
    public TextView tv_title;

    /**
     * 点击侧滑的
     */
    public ImageButton ib_menu;

    /**
     * 加载各个子页面
     */
    public FrameLayout fl_content;
    /**
     * 每个子页面
     */
    public View rootView;

    /**
     * 图组ListView和GridView切换按钮
     */
    public ImageButton ib_list_grid;
    //    private ImageButton ib_grid;
    public Button btn_cart_edit;
    public Button btn_cart_over;


    public BasePager(Context context) {
        this.context = context;
        rootView = initView();

    }

    private View initView() {

        View view = View.inflate(context, R.layout.base_pager, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        ib_list_grid = (ImageButton) view.findViewById(R.id.ib_list_grid);
        btn_cart_edit = (Button) view.findViewById(R.id.btn_cart_edit);
        btn_cart_over = (Button) view.findViewById(R.id.btn_cart_over);
//        ib_grid = (ImageButton) view.findViewById(R.id.ib_grid);
        return view;
    }

    /**
     * 用于子ViewPager继承来初始化数据
     */
    public void initData() {

    }


}
