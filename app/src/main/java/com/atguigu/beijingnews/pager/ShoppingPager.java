package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.ShoppingPagerAdapter;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.domain.ShoppingPagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.common.util.LogUtil;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：商城
 */
public class ShoppingPager extends BasePager {

    /**
     * 商城url地址
     */
    private String url;
    /**
     * 当前页的信息条数  默认10条
     */
    private int pageSize = 10;
    /**
     * 当前第几页
     */
    private int curPage = 1;
    /**
     * 总的多少页
     */
    private int totalPage;
    /**
     * 商城信息
     */
    private List<ShoppingPagerBean.Wares> listBeans;
    /**
     * 默认状态
     */
    private static final int STATE_NORMAL = 1;
    /**
     * 下拉刷新状态
     */
    private static final int STATE_REFRESH = 2;
    /**
     * 加载更多状态
     */
    private static final int STATE_LOADMOR = 3;
    /**
     * 当前状态  默认是默认状态
     */
    private static int currentState = STATE_NORMAL;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ShoppingPagerAdapter adapter;
    private MaterialRefreshLayout refresh;

    public ShoppingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("商城数据被初始化了...");
        //1.设置标题
        tv_title.setText("商城");
        //2.联网请求，得到数据，创建视图
        View view = View.inflate(context, R.layout.shopping_pager, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.removeAllViews();
        fl_content.addView(view);
        //4.绑定数据

        //设置下拉刷新和上拉刷新的监听
        refresh.setMaterialRefreshListener(new MyMaterialRefreshListener());

        getDataFromNet();
    }

    class MyMaterialRefreshListener extends MaterialRefreshListener {

        /**
         * 下拉刷新
         *
         * @param materialRefreshLayout
         */
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();
        }

        /**
         * 加载更多
         *
         * @param materialRefreshLayout
         */
        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            loadMoreData();
        }
    }

    //加载更多
    private void loadMoreData() {
        currentState = STATE_LOADMOR;
        if (curPage < totalPage) {
            //有更多数据
            curPage++;
            //联网加载
            url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage;
            OkHttpUtils
                    .get()
                    .url(url)
                    .id(100)
                    .build()
                    .execute(new MyStringCallback());
        } else {
            //没有更多数据
            refresh.finishRefreshLoadMore();
            Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
        }


    }

    //下拉刷新
    private void refreshData() {
        curPage = 1;
        currentState = STATE_REFRESH;
        url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage;
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public void getDataFromNet() {
        currentState = STATE_NORMAL;
        //http://112.124.22.238:8081/course_api/wares/hot?pageSize=10&curPage=1
        curPage = 1;
        //本地文件存储
        String saveJson = CacheUtils.getString(context, Constants.WARES_HOT_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processJson(saveJson);
        }
        url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage;
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e, int id) {
            com.atguigu.beijingnews.utils.LogUtil.e("okhttpUtils互动请求数据失败==" + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(String s, int id) {

            com.atguigu.beijingnews.utils.LogUtil.e("okhttpUtils互动请求数据成功==" + s);
            //解析json数据
            CacheUtils.putString(context, Constants.WARES_HOT_URL, s);
            processJson(s);
            switch (id) {
                case 100:
                    Toast.makeText(context, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(context, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void processJson(String json) {
        ShoppingPagerBean bean = new Gson().fromJson(json, ShoppingPagerBean.class);
        curPage = bean.getCurrentPage();
        pageSize = bean.getPageSize();
        totalPage = bean.getTotalPage();
        listBeans = bean.getList();
        if (listBeans != null && listBeans.size() > 0) {
            LogUtil.e("有数据:curPage = " + curPage + ",pageSize = " + pageSize + ",totalPage = " + totalPage + ",listBeans =" + listBeans.toString());
            showData();

        }
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {

        switch (currentState){
            case STATE_NORMAL://默认模式
                //有数据
                adapter = new ShoppingPagerAdapter(context, listBeans);
                //设置设配器
                recyclerView.setAdapter(adapter);

                //设置ListView样式
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                break;
            case STATE_REFRESH://下拉刷新
                //清除数据
                adapter.clearData();
                //重新添加数据
                adapter.addData(listBeans);
                //取消刷新状态
                refresh.finishRefresh();
                break;
            case STATE_LOADMOR://加载更多
                adapter.addData(adapter.getCount(),listBeans);
                //取消加载更多的状态
                refresh.finishRefreshLoadMore();
                break;

        }

    }
}
