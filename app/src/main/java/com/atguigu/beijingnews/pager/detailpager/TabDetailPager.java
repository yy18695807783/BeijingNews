package com.atguigu.beijingnews.pager.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;
import com.atguigu.beijingnews.domain.TableDetailPagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.atguigu.listviewrefresh_library.RefreshListView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by 颜银 on 2016/10/18.
 * QQ:443098360
 * 微信：y443098360
 * 作用：12个页签页面
 */
public class TabDetailPager extends MenuDetailBasePager {


    public static final String READ_ARRAY_ID = "read_array_id";
    private final NewsCenterPagerBean2.DataBean.ChildrenBean childrenBean;
    private final ImageOptions imageOptions;
//    private TextView textView;

    @ViewInject(R.id.viewpager)
    private HorizontalScrollViewPager viewPager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_point_group)
    private LinearLayout ll_point_group;

    @ViewInject(R.id.listview)
    private RefreshListView listview;
    /**
     * 顶部新闻数据集合
     */
    private List<TableDetailPagerBean.DataBean.TopnewsBean> topnews;
    /**
     * 记录之前的信息位置 之前被高亮显示的点
     */
    private int perPosition;
    /**
     * listView的新闻数据集合
     */
    private List<TableDetailPagerBean.DataBean.NewsBean> newsBeans;
    /**
     * listView的适配器
     */
    private TabDetailListViewAdapter adapter;
    /**
     * 请求网络数据的url
     */
    private String url;
    /**
     * 加载更多的url
     */
    private String moreUrl;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;
    /**
     * 发送消息的handler类
     */
    private InternalHandler handler;


    public TabDetailPager(Context context, NewsCenterPagerBean2.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(80), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                        // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                        // 加载中或错误图片的ScaleType
                        //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                        //设置使用缓存
                .setUseMemCache(true)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
//        textView = new TextView(context);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(30);
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        x.view().inject(this, view);

        View topnew = View.inflate(context, R.layout.topnews, null);
        x.view().inject(this, topnew);

//        Button button = new Button(context);
//        button.setText("我是按钮");
//        listview.addHeaderView(button);

        //以头的方式添加顶部轮播图
//        listview.addHeaderView(topnew);


        listview.addTopNewsView(topnew);

        //设置刷新的监听
        listview.setOnRefreshListener(new MyOnRefreshListener());

        //liseView的item点击监听
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int relPosition = position - 1;
            TableDetailPagerBean.DataBean.NewsBean newsBean = newsBeans.get(relPosition);
//            Toast.makeText(context, "newsBean:: Title==" + newsBean.getTitle() + ",position=" + position, Toast.LENGTH_SHORT).show();

            //1.取出保存集合的id
            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);
            //2.判断是否存在，不存在保存 并刷新适配器
            if (!idArray.contains(newsBean.getId() + "")) ;
            {
                CacheUtils.putString(context, READ_ARRAY_ID, idArray + newsBean.getId() + ",");
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constants.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);

        }
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {
        /**
         * 下拉刷新
         */
        @Override
        public void onPullDownRefresh() {
            //请求网络
            getDataFromNet(url);
        }

        /**
         * 加载更多
         */
        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(moreUrl)) {
                //没有更多数据
                Toast.makeText(context, "没有更多数据可以加载", Toast.LENGTH_SHORT).show();
                listview.onFinishRefrsh(false);
            } else {
                //有数据 加载更多
                getModeDataFromNet();
            }
        }
    }

    private void getModeDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("TabDetailPager联网请求成功==" + result);
                isLoadMore = true;
                processData(result);
                listview.onFinishRefrsh(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("TabDetailPager联网请求失败==" + ex.getMessage());
                listview.onFinishRefrsh(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
//        textView.setText(childrenBean.getTitle());

        url = Constants.BASE_URL + childrenBean.getUrl();
        LogUtil.e("---TabDetailPager" + url);

        //获取缓存数据
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        //请求联网xUtils
        getDataFromNet(url);


    }


    private void getDataFromNet(String json) {
        RequestParams params = new RequestParams(json);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("TabDetailPager联网请求成功==" + result);
                //缓存
                CacheUtils.putString(context, url, result);
                processData(result);
                listview.onFinishRefrsh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("TabDetailPager联网请求失败==" + ex.getMessage());
                listview.onFinishRefrsh(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    private void processData(String json) {
        TableDetailPagerBean pagerBean = paraseJson(json);

//        //顶部新闻数据集合   需要放到  放到加载里面
//        topnews = pagerBean.getData().getTopnews();

        moreUrl = pagerBean.getData().getMore();
        if (TextUtils.isEmpty(moreUrl)) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + moreUrl;
        }

        if (!isLoadMore) {

            //顶部新闻数据集合   需要放到if里面
            topnews = pagerBean.getData().getTopnews();

            //原来的请求
            if (topnews != null && topnews.size() > 0) {
                viewPager.setAdapter(new TabDetailViewPagerAdapter());

                //监听页面的变化
                viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

                //默认选择第一个标题  perPosition刚刚开始没有上一个位置 默认为0 没有这行代码，标题会写xml文件中的标题
                tv_title.setText(topnews.get(perPosition).getTitle());

                //把之前的红点全部移除  否则会回翻看会出现点多的bug
                ll_point_group.removeAllViews();
                //动态添加点
                addPoint();

                //设置listView的数据 及适配器
                newsBeans = pagerBean.getData().getNews();
                if (newsBeans != null && newsBeans.size() > 0) {
                    adapter = new TabDetailListViewAdapter();
                    listview.setAdapter(adapter);
                }
            }

//        LogUtil.e(pagerBean.getData().getTopnews().get(1).getTitle());
        } else {
            //加载更多
            isLoadMore = false;
            newsBeans.addAll(pagerBean.getData().getNews());
            adapter.notifyDataSetChanged();
        }

        //发消息每隔4000切换一次ViewPager页面
        if (handler == null) {
            handler = new InternalHandler();
        }
        //是把消息队列所有的消息和回调移除

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new MyRunnable(), 4000);

    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item = (viewPager.getCurrentItem() + 1) % topnews.size();
            viewPager.setCurrentItem(item);

            handler.postDelayed(new MyRunnable(), 4000);
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {

            handler.sendEmptyMessage(0);

        }
    }

    /**
     * 动态添加点的方法
     */
    private void addPoint() {
        for (int i = 0; i < topnews.size(); i++) {
            //动态添加点
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(5);
                point.setEnabled(false);
            } else {
                point.setEnabled(true);
            }
            point.setLayoutParams(params);
            //把点添加到线性布局
            ll_point_group.addView(point);
        }
    }

    class TabDetailListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsBeans == null ? 0 : newsBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //装配数据
            TableDetailPagerBean.DataBean.NewsBean newsBean = newsBeans.get(position);
            viewHolder.tv_time.setText(newsBean.getPubdate());
            viewHolder.tv_title.setText(newsBean.getTitle());
            //请求图片
//            x.image().bind(viewHolder.iv_icon, Constants.BASE_URL + newsBean.getListimage(), imageOptions);

            //请求图片使用glide
            Glide.with(context)
                    .load(Constants.BASE_URL + newsBean.getListimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.iv_icon);


            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);
            if (idArray.contains(newsBean.getId() + "")) {
                //设置成灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                //设置成黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        ImageView iv_icon;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //设置之前高亮的点变为默认
            ll_point_group.getChildAt(perPosition).setEnabled(false);
            //设置当前页面对应点的点高亮
            ll_point_group.getChildAt(position).setEnabled(true);

            perPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            tv_title.setText(topnews.get(position).getTitle());
        }

        private boolean isDragging = false;

        //拖拽状态改变
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {//拖拽状态
                isDragging = true;
                //拖拽状态时就移除所有消息
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {//空闲状态
                isDragging = false;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                handler.postDelayed(new MyRunnable(), 4000);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {//惯性状态
                isDragging = false;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                handler.postDelayed(new MyRunnable(), 4000);
            }
        }
    }

    class TabDetailViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews == null ? 0 : topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.pic_item_list_default);
            x.image().bind(imageView, Constants.BASE_URL + topnews.get(position).getTopimage());

            container.addView(imageView);
//
//            //请求图片使用glide
//            Glide.with(context)
//                    .load(Constants.BASE_URL + topnews.get(position).getTopimage())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.news_pic_default)
//                    .error(R.drawable.news_pic_default)
//                    .into(imageView);



            //设置触摸事件
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //取消所有消息
                            if (handler != null) {
                                handler.removeCallbacksAndMessages(null);
                            }

                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;
                        case MotionEvent.ACTION_UP:
                            //松手重新开始发送消息
                            if (handler != null) {
                                handler.removeCallbacksAndMessages(null);
                            }
                            handler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }
                    return false;//true会在ViewPager滑动时丢失消息事件。 false 点击事件可以生效
                }
            });

            //设置点击事件
            imageView.setTag(position);
            imageView.setOnClickListener(new TabDetailPagerOnClickListener());

            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    private TableDetailPagerBean paraseJson(String json) {
        Gson gson = new Gson();
        TableDetailPagerBean pagerBean = gson.fromJson(json, TableDetailPagerBean.class);
        return pagerBean;
    }

    class TabDetailPagerOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TableDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            Intent intent = new Intent(context, NewsDetailActivity.class);
//            intent.setData(Uri.parse(Constants.BASE_URL+topnewsBean.getUrl()));
            intent.putExtra("url", Constants.BASE_URL + topnewsBean.getUrl());
            LogUtil.e("TabDetailPagerOnClickListener=====" + Constants.BASE_URL + topnewsBean.getUrl());
            context.startActivity(intent);
        }
    }
}
