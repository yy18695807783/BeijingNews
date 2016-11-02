package com.atguigu.listviewrefresh_library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 颜银 on 2016/10/19.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class RefreshListView extends ListView {

    private View pullDownRefresh;
    private View loadMoreRefresh;
    private ImageView iv_red_arrow;
    private ProgressBar progressbar;
    private TextView tv_status;
    private TextView tv_time;

    /**
     * 下拉刷新状态
     */
    private static final int PULL_DOWN_REFRESH = 0;
    /**
     * 手松刷新状态
     */
    private static final int RELEASE_REFRESH = 1;
    /**
     * 正在刷新状态
     */
    private static final int REFRESHING = 2;
    /**
     * 当前状态  默认为下拉刷新
     */
    private static int currentStatus = PULL_DOWN_REFRESH;

    /**
     * 头部刷新控件的高
     */
    private int headerViewHeight;
    /**
     * 尾部刷新控件的高
     */
    private int footerViewHeight;
    /**
     * 是否加载更多  true 加载更多
     */
    private boolean isLoadMore = false;
    /**
     * 尾部加载控件
     */
    private View footerView;
    /**
     * 头顶的轮番图
     */
    private View topnew;
    /**
     * listView的头不是图
     */
    private LinearLayout headerView;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.foot_refresh, null);

        //默认是隐藏
//        View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);//默认是隐藏

        addFooterView(footerView);

        //设置监听滑动底部
        setOnScrollListener(new MyOnScrollListener());
    }

    public void addTopNewsView(View topnew) {
        this.topnew = topnew;
        headerView.addView(topnew);
    }

    private int LisviewOnScreenY = -1;

    /**
     * 判断是否完全显示顶部轮播图部分
     * 当ListView在Y轴的坐标小于或等于顶部轮播图在Y轴坐标的时候，就完全显示
     *
     * @return
     */
    public boolean isDisplayTopNews() {
        if(topnew!=null){
            int[] location = new int[2];
            //得到ListView在屏幕的Y轴坐标
            if (LisviewOnScreenY == -1) {
                this.getLocationOnScreen(location);
                LisviewOnScreenY = location[1];
//            LogUtil.e("LisviewOnScreenY ==location[0] =" + location[0] + ",location[1]=" + location[1]);
            }
            //得到顶部轮播部分在屏幕的Y轴坐标
            topnew.getLocationOnScreen(location);
            int topnewOnScreenY = location[1];
//        LogUtil.e("topnewOnScreenY ==location[0] =" + location[0] + ",location[1]=" + location[1]);

//        if(LisviewOnScreenY <= topnewOnScreenY){
//            return true;
//        }else {
//            return false;
//        }

            return LisviewOnScreenY <= topnewOnScreenY;
        }else {
            return true;
        }

    }

    class MyOnScrollListener implements OnScrollListener {

        /**
         * ListView当状态变化的时候回调
         * 静止-->手指滑动
         * 手指滑动-->惯性滚动
         * 惯性滚动-->静止
         *
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                //并且是滑动到最后一条的时候
                //当滑动到最后一个可见并且等于集合中最后一条位置的时候
                if (getLastVisiblePosition() == getAdapter().getCount() - 1) {
                    //1.状态修改加载更多
                    isLoadMore = true;
                    //2.显示加载更多控件
                    footerView.setPadding(0, 0, 0, 0);
                    //3.回调接口
                    if (refreshListener != null) {
                        refreshListener.onLoadMore();
                    }

                }
            }
        }

        /**
         * ListView正在滚动的时候回调
         *
         * @param view
         * @param firstVisibleItem
         * @param visibleItemCount
         * @param totalItemCount
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    //箭头向上
    private Animation upAnimation;
    //箭头向下
    private Animation downAnimation;

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.head_refresh, null);

        pullDownRefresh = headerView.findViewById(R.id.ll_pull_refresh);
        loadMoreRefresh = headerView.findViewById(R.id.load_more_refresh);
        iv_red_arrow = (ImageView) headerView.findViewById(R.id.iv_red_arrow);
        progressbar = (ProgressBar) headerView.findViewById(R.id.progressbar);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

//        View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        pullDownRefresh.measure(0, 0);//测量控件的高 参数无实际意义，占位
        headerViewHeight = pullDownRefresh.getMeasuredHeight();
        pullDownRefresh.setPadding(0, -headerViewHeight, 0, 0);//默认隐藏

        addHeaderView(headerView);
    }

    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录其实位置
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                float endY = ev.getY();
                float diatanceY = endY - startY;

                //判断顶部轮播图是否完全显示
                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews) {
                    break;
                }


                if (currentStatus == REFRESHING) {
                    break;
                }
                //判断
                if (diatanceY > 0) {
                    //下拉过程
                    //float paddingTop = -控件的高 + distanceY
                    float paddingTop = -headerViewHeight + diatanceY;
//                    LogUtil.e("paddingTop===" + paddingTop + "'-headerViewHeight =" + -headerViewHeight +"");
                    if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //手松刷新
                        currentStatus = RELEASE_REFRESH;
                        refreshView(currentStatus);
//                        LogUtil.e("手松刷新...");
                    } else if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新
                        currentStatus = PULL_DOWN_REFRESH;
                        refreshView(currentStatus);
//                        LogUtil.e("下拉刷新...");
                    }
                    // View.setPaddingTop(0,paddingTop，0,0);//完成显示
                    pullDownRefresh.setPadding(0, (int) paddingTop, 0, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (currentStatus == PULL_DOWN_REFRESH) { //还没出来 隐藏
                    pullDownRefresh.setPadding(0, -headerViewHeight, 0, 0);//隐藏
                } else if (currentStatus == RELEASE_REFRESH) {//手松刷新  出来啦  松手显示正在刷新
                    //状态改变-->正在加载
                    currentStatus = REFRESHING;
                    //显示-->全部显示
                    pullDownRefresh.setPadding(0, 0, 0, 0);
                    //页面-->显示正在加载页面
                    refreshView(currentStatus);
                    //回调接口由接口的实现类去联网请求
                    if (refreshListener != null) {
                        refreshListener.onPullDownRefresh();
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshView(int currentStatus) {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH://下拉刷新...  显示箭头  向下
                tv_status.setText("下拉刷新...");
                iv_red_arrow.setVisibility(VISIBLE);
                iv_red_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH://手松刷新... 显示箭头  向上
                tv_status.setText("手松刷新...");
                iv_red_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING://正在刷新...  显示progressbar 不显示箭头
                tv_status.setText("正在刷新...");
                progressbar.setVisibility(VISIBLE);
                iv_red_arrow.clearAnimation();
                iv_red_arrow.setVisibility(GONE);
                break;
        }
    }

    /**
     * 下拉刷新完成后的回调
     *
     * @param sucess
     */
    public void onFinishRefrsh(boolean sucess) {
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            footerView.setPadding(0, -footerViewHeight, 0, 0);//设置隐藏
        } else {
            //下拉刷新
            currentStatus = PULL_DOWN_REFRESH;
            pullDownRefresh.setPadding(0, -headerViewHeight, 0, 0);//把下拉刷新控件隐藏
            progressbar.setVisibility(GONE);
            iv_red_arrow.setVisibility(VISIBLE);
            tv_status.setText("下拉刷新...");
            if (sucess) {
                tv_time.setText("上次更新时间:" + getTimeFromSystem());
            }
        }

    }

    public String getTimeFromSystem() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }


    /**
     * 刷新的监听器
     */
    public interface OnRefreshListener {
        /**
         * 当下拉刷新的时候回调这个方法
         */
        public void onPullDownRefresh();

        /**
         * 当家在更多的时候的回调方法
         */
        void onLoadMore();
    }

    private OnRefreshListener refreshListener;

    /**
     * 设置刷新的监听：下拉刷新和上拉刷新（加载更多）
     *
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
}
