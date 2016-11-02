package com.atguigu.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnList;
    private Button btnGlide;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnFlow;
    private RecyclerView recycleView;

    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 集合数据
     */
    private ArrayList<String> datas;
    /**
     * 集合适配器
     */
    private RecyclerViewAdapter adapter;
    /**
     * RecycleVierw的分割线
     */
    private DividerItemDecoration dividerItemDecoration;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-10-25 18:49:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_main);
        btnList = (Button) findViewById(R.id.btn_list);
        btnGlide = (Button) findViewById(R.id.btn_glide);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnFlow = (Button) findViewById(R.id.btn_flow);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        btnList.setOnClickListener(this);
        btnGlide.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnFlow.setOnClickListener(this);

        //设置SwipeRefreshLayout 下拉刷新
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        //设置小圈圈颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light,android.R.color.holo_orange_light);

        //两种模式：默认和大模式
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        //设置整个大圈颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.white));

        //设置滑动的距离
        swipeRefreshLayout.setDistanceToTriggerSync(100);

        //设置监听下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListener());
    }
    class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {

            datas = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                datas.add("new Data_" + i);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //设置适配器
                    adapter = new RecyclerViewAdapter(MainActivity.this, datas);
                    recycleView.setAdapter(adapter);

                    //设置LayoutManger
                    //ListView
                    recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

                    //还原状态
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);


        }
    }


    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-10-25 18:49:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnList) {
            recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
            // Handle clicks for btnList
        } else if (v == btnGlide) {
            recycleView.setLayoutManager(new GridLayoutManager(MainActivity.this,2,LinearLayoutManager.VERTICAL,false));
            // Handle clicks for btnGlide
        } else if (v == btnFlow) {
            recycleView.setLayoutManager(new StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL));
            // Handle clicks for btnFlow
        }else if (v == btnAdd) {
            adapter.addData(0,"new Data");
            recycleView.scrollToPosition(0);//定位到第一条信息
            // Handle clicks for btnAdd
        } else if (v == btnDelete) {
            adapter.deleteData(0);
            recycleView.scrollToPosition(0);
            // Handle clicks for btnDelete
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();

        //初始化数据
        datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("颜银" + i);
        }
        //设置适配器
        adapter = new RecyclerViewAdapter(this,datas);
        recycleView.setAdapter(adapter);

        //设置LayoutManger
        //ListView  第一个参数：上下文  第二个参数：竖直还是水平方向   第三个参数：是否倒叙
        recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        //GlideView   第一个参数：上下文  第二个参数：行列数   第三个参数：竖直还是水平方向   第四个参数：是否倒叙
//        recycleView.setLayoutManager(new GridLayoutManager(MainActivity.this,2,LinearLayoutManager.VERTICAL,false));
        //瀑布流   第一个参数：行列数   第二个参数：竖直还是水平方向
//        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));


        //分割线
        dividerItemDecoration = new DividerItemDecoration(MainActivity.this,LinearLayoutManager.VERTICAL);
        recycleView.addItemDecoration(dividerItemDecoration);

    }
}
