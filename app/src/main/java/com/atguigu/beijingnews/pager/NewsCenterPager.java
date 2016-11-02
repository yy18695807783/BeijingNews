package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.pager.detailpager.InteractDetailPager;
import com.atguigu.beijingnews.pager.detailpager.NewsDetailPager;
import com.atguigu.beijingnews.pager.detailpager.PhotosDetailPager;
import com.atguigu.beijingnews.pager.detailpager.TopicDetailPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：新闻中心
 */
public class NewsCenterPager extends BasePager {

    /**
     * 左侧菜单对应数据
     */
//    private List<NewsCenterPagerBean.DataBean> leftData;
    private List<NewsCenterPagerBean2.DataBean> leftData;


    private List<MenuDetailBasePager> detailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了...");

        //显示ImageButton
        ib_menu.setVisibility(View.VISIBLE);

        ib_menu.setOnClickListener(new MyOnClickListener());
        //1.设置标题
        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("新闻中心内容");

        //获取缓存的数据
        String saveJson = CacheUtils.getString(context, Constants.NEWS_CENTER_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        //联网加载数据
//        getDataFromNet();

        //用Volley来联网
        getDataFromNetByVolley();
    }

    public void getDataFromNetByVolley() {
//        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.NEWS_CENTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("Volley请求数据成功，result ==" + result);
                //缓存数据
                CacheUtils.putString(context, Constants.NEWS_CENTER_URL, result);
                //解析数据
                processData(result);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("Volley请求数据失败，result ==" + volleyError.getMessage());
            }
        }){
            //重新配置编码
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                    parsed = new String(response.data);
                }
                return super.parseNetworkResponse(response);
            }
        };

        //把StringRequest添加到队列中
//        queue.add(request);
        VolleyManager.getRequestQueue().add(request);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.getSlidingMenu().toggle();//开<--->关
        }
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWS_CENTER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("请求数据成功，result ==" + result);

                //缓存数据
                CacheUtils.putString(context, Constants.NEWS_CENTER_URL, result);
                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("请求数据失败，result ==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void processData(String json) {


//        NewsCenterPagerBean bean = paraseJson(json);
//        LogUtil.e("解析成功了==" + bean.getData().get(0).getChildren().get(1).getTitle());
        NewsCenterPagerBean2 bean2 = paraseJson2(json);
//        NewsCenterPagerBean2 bean3 = paraseJson3(json);
        LogUtil.e("解析成功了==" + bean2.getData().get(0).getChildren().get(1).getTitle());

        //把解析好的数据传递给左侧菜单
        leftData = bean2.getData();

        MainActivity mainActivity = (MainActivity) context;

        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsDetailPager(context, leftData.get(0)));//新闻详情页面
        detailBasePagers.add(new TopicDetailPager(context, leftData.get(0)));//专题详情页面
        detailBasePagers.add(new PhotosDetailPager(context, leftData.get(2)));//组图详情页面
        detailBasePagers.add(new InteractDetailPager(context, leftData.get(2)));//互动详情页面

        //得到左侧菜单的实例
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(leftData);

        //默认显示第一页
        switchPager(0);

    }

    private NewsCenterPagerBean2 paraseJson2(String json) {
//        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            int retcode = jsonObject.optInt("retcode");
//            bean2.setRetcode(retcode);
//
//            JSONArray jsonArrayData = jsonObject.optJSONArray("data");
//            if (jsonArrayData != null) {
//                List<NewsCenterPagerBean2.DataBean> dataBeans = new ArrayList<>();
//
//                bean2.setData(dataBeans);
//                for (int i = 0; i < jsonArrayData.length(); i++) {
//                    JSONObject itemData = (JSONObject) jsonArrayData.get(i);
//                    if (itemData != null) {
//                        NewsCenterPagerBean2.DataBean dataBean = new NewsCenterPagerBean2.DataBean();
//                        int id = itemData.optInt("id");
//                        dataBean.setId(id);
//                        int type = itemData.optInt("type");
//                        dataBean.setType(type);
//                        String title = itemData.optString("title");
//                        dataBean.setTitle(title);
//                        String url = itemData.optString("url");
//                        dataBean.setUrl(url);
//                        String url1 = itemData.optString("url1");
//                        dataBean.setUrl1(url1);
//                        String dayurl = itemData.optString("dayurl");
//                        dataBean.setDayurl(dayurl);
//                        String excurl = itemData.optString("excurl");
//                        dataBean.setExcurl(excurl);
//                        String weekurl = itemData.optString("weekurl");
//                        dataBean.setWeekurl(weekurl);
//
//
//                        JSONArray jsonArraychildren = itemData.optJSONArray("children");
//                        if (jsonArraychildren != null) {
//                            List<NewsCenterPagerBean2.DataBean.ChildrenBean> childrenBeans = new ArrayList<>();
//                            dataBean.setChildren(childrenBeans);
//                            for (int j = 0; j < jsonArraychildren.length(); j++) {
//                                JSONObject itemchildren = (JSONObject) jsonArraychildren.get(j);
//                                if (itemchildren != null) {
//                                    NewsCenterPagerBean2.DataBean.ChildrenBean childrenBean = new NewsCenterPagerBean2.DataBean.ChildrenBean();
//
//                                    // 添加到集合中
//                                    childrenBeans.add(childrenBean);
//                                    //添加数据
//                                    childrenBean.setId(itemchildren.optInt("id"));
//                                    childrenBean.setType(itemchildren.optInt("type"));
//                                    childrenBean.setTitle(itemchildren.optString("title"));
//                                    childrenBean.setUrl(itemchildren.optString("url"));
//                                }
//                            }
//
//                        }
//                        dataBeans.add(dataBean);
//                    }
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            bean2.setRetcode(retcode);


            JSONArray jsonArrayData = jsonObject.optJSONArray("data");
            if (jsonArrayData != null) {

                //创建集合装数据
                List<NewsCenterPagerBean2.DataBean> data = new ArrayList<>();
                //把集合关联到Bean对象中
                bean2.setData(data);

                for (int i = 0; i < jsonArrayData.length(); i++) {

                    JSONObject itemData = (JSONObject) jsonArrayData.get(i);
                    if (itemData != null) {

                        NewsCenterPagerBean2.DataBean newsCenterPagerData = new NewsCenterPagerBean2.DataBean();

                        int id = itemData.optInt("id");
                        newsCenterPagerData.setId(id);
                        int type = itemData.optInt("type");
                        newsCenterPagerData.setType(type);
                        String title = itemData.optString("title");
                        newsCenterPagerData.setTitle(title);
                        String url = itemData.optString("url");
                        newsCenterPagerData.setUrl(url);
                        String url1 = itemData.optString("url1");
                        newsCenterPagerData.setUrl1(url1);
                        String dayurl = itemData.optString("dayurl");
                        newsCenterPagerData.setDayurl(dayurl);
                        String excurl = itemData.optString("excurl");
                        newsCenterPagerData.setExcurl(excurl);
                        String weekurl = itemData.optString("weekurl");
                        newsCenterPagerData.setWeekurl(weekurl);


                        JSONArray childrenjsonArray = itemData.optJSONArray("children");
                        if (childrenjsonArray != null) {
                            List<NewsCenterPagerBean2.DataBean.ChildrenBean> children = new ArrayList<>();
                            //设置children的数据
                            newsCenterPagerData.setChildren(children);

                            for (int j = 0; j < childrenjsonArray.length(); j++) {

                                JSONObject chilrenItemData = (JSONObject) childrenjsonArray.get(j);

                                if (chilrenItemData != null) {

                                    NewsCenterPagerBean2.DataBean.ChildrenBean childrenData = new NewsCenterPagerBean2.DataBean.ChildrenBean();

                                    //添加到集合中
                                    children.add(childrenData);
                                    //添加数据
                                    childrenData.setId(chilrenItemData.optInt("id"));
                                    childrenData.setType(chilrenItemData.optInt("type"));
                                    childrenData.setTitle(chilrenItemData.optString("title"));
                                    childrenData.setUrl(chilrenItemData.optString("url"));
                                }

                            }

                        }

                        //把数据添加到集合中
                        data.add(newsCenterPagerData);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean2;
    }

    private NewsCenterPagerBean2 paraseJson3(String json) {
        return new Gson().fromJson(json, NewsCenterPagerBean2.class);
    }

    /**
     * 解析json数据：使用第三方框架（Gson,Fastjson）和使用系统的API解析
     *
     * @param json
     * @return
     */
    private NewsCenterPagerBean paraseJson(String json) {
//        Gson gson = new Gson();
//        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);
//        return bean;
        return new Gson().fromJson(json, NewsCenterPagerBean.class);
    }

    /**
     * 根据位置切换到不同的详情页面
     *
     * @param position
     */
    public void switchPager(int position) {
        //改变标题
        tv_title.setText(leftData.get(position).getTitle());
        //改变内容
        MenuDetailBasePager detailBasePager = detailBasePagers.get(position);//NewsDeatailPager,TopicDeatailPager
        View rootView = detailBasePager.rootView;
        detailBasePager.initData();//初始化数据
        //把之前的移除
        fl_content.removeAllViews();
        //添加到FrameLayout
        fl_content.addView(rootView);

        if(position == 2){
            ib_list_grid.setVisibility(View.VISIBLE);
            ib_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotosDetailPager photosDetailPager = (PhotosDetailPager) detailBasePagers.get(2);
                    photosDetailPager.switchListAndGrid(ib_list_grid);
                }
            });
        }else {
            ib_list_grid.setVisibility(View.GONE);
        }

    }
}
