package com.atguigu.beijingnews.pager.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.ShowImageActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;
import com.atguigu.beijingnews.domain.PhotosDetailPagerBean;
import com.atguigu.beijingnews.utils.BitmapCacheUtils;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constants;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.utils.NetCacheUtils;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 颜银 on 2016/10/17.
 * QQ:443098360
 * 微信：y443098360
 * 作用：互动详情页面
 */
public class InteractDetailPager extends MenuDetailBasePager {


    private final NewsCenterPagerBean2.DataBean dataBean;
    private final DisplayImageOptions options;

    @ViewInject(R.id.listview)
    private ListView listView;
    @ViewInject(R.id.gridview)
    private GridView gridView;
    /**
     * 图片专题页面的url
     */
    private String photosUrl;
    private List<PhotosDetailPagerBean.DataBean.NewsBean> news;

    /**
     * 三级缓存工具类
     */
    private BitmapCacheUtils bitmapCacheUtils;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    LogUtil.e("图片网络请求成功");
                    if (listView != null && listView.isShown()) {
                        ImageView imageView = (ImageView) listView.findViewWithTag(position);
                        if (imageView != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAIL:
                    position = msg.arg1;
                    LogUtil.e("图片网络请求失败");
                    break;
            }
        }
    };

    public InteractDetailPager(Context context, NewsCenterPagerBean2.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
        //volley
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default)//正在加载中显示的图片
                .showImageForEmptyUri(R.drawable.pic_item_list_default)//空Uri时显示的图片
                .showImageOnFail(R.drawable.pic_item_list_default)//加载错误显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))//矩形圆角图片
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.interact_detail_pager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        photosUrl = Constants.BASE_URL + dataBean.getUrl();
        LogUtil.e("photosUrl==" + photosUrl.toString());

        //本地文件存储
        String saveJson = CacheUtils.getString(context, photosUrl);
        if (!TextUtils.isEmpty(saveJson)) {
            processJson(saveJson);
        }

//        StringRequest request = new StringRequest(Request.Method.GET, photosUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                LogUtil.e("图组请求数据成功==" + s);
//                CacheUtils.putString(context,photosUrl,s);
//                processJson(s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                LogUtil.e("图组请求数据失败==" + volleyError.getMessage());
//            }
//        }) {
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String parsed;
//                try {
//                    parsed = new String(response.data, "UTF-8");
//                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
//                } catch (UnsupportedEncodingException var4) {
//                    parsed = new String(response.data);
//                }
//
//                return super.parseNetworkResponse(response);
//            }
//        };
//        VolleyManager.getRequestQueue().add(request);

        getDataFromNetByOkHttpUtils();

    }

    /**
     * okhttp请求网络
     */
    public void getDataFromNetByOkHttpUtils() {
        OkHttpUtils
                .get()
                .url(photosUrl)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(okhttp3.Request request, int id) {
        }

        @Override
        public void onAfter(int id) {
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("okhttpUtils互动请求数据失败==" + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(String s, int id) {

            LogUtil.e("okhttpUtils互动请求数据成功==" + s);
            //解析json数据
            CacheUtils.putString(context, photosUrl, s);
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

        @Override
        public void inProgress(float progress, long total, int id) {
        }
    }


    private void processJson(String json) {
        PhotosDetailPagerBean bean = new Gson().fromJson(json, PhotosDetailPagerBean.class);
        news = bean.getData().getNews();
        if (news != null && news.size() > 0) {
            //设置适配器
            listView.setAdapter(new PhotosDetailPagerAdapter());
            listView.setOnItemClickListener(new PhotosDetailPagerListViewOnItemClickListener());
        }
    }


    class PhotosDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news == null ? 0 : news.size();
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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                if (isListview) {
                    //listView
                    LogUtil.e("listView-------------");
                    convertView = View.inflate(context, R.layout.item_photos_detail_pager, null);
                } else {
                    //GridView
                    LogUtil.e("GridView-------------");
                    convertView = View.inflate(context, R.layout.item_photos_detail_pager2, null);
                }

                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //取出数据装配数据
            PhotosDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);

            viewHolder.tv_title.setText(newsBean.getTitle());
//            loadImager(viewHolder, Constants.BASE_URL + newsBean.getListimage());


            //1.自定义三级缓存的的优化方法
//            viewHolder.iv_icon.setTag(position);
//            Bitmap bitmap = bitmapCacheUtils.getBitmap(Constants.BASE_URL + newsBean.getListimage(), position);
//            if(bitmap!=null){//来自内存或者本地
//                viewHolder.iv_icon.setImageBitmap(bitmap);
//            }

            //2.使用Picasso请求图片
//            Picasso.with(context)
//                    .load(Constants.BASE_URL + newsBean.getListimage())
//                    .placeholder(R.drawable.pic_item_list_default)
//                    .error(R.drawable.pic_item_list_default)
//                    .into(viewHolder.iv_icon);

            //3.使用glide请求图片
//            Glide.with(context)
//                    .load (Constants. BASE_URL + newsBean.getListimage ())
//                    .diskCacheStrategy (DiskCacheStrategy.ALL)
//                    .placeholder (R.drawable. news_pic_default)
//                    .error (R.drawable. news_pic_default)
//                    .into (viewHolder. iv_icon);

            //使用 imageLoader请求网络图片
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(Constants.BASE_URL + newsBean.getListimage(), viewHolder.iv_icon, options);
            return convertView;
        }
    }

    private void loadImager(final ViewHolder viewHolder, String imagerUrl) {
        viewHolder.iv_icon.setTag(imagerUrl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
            }
        };
        VolleyManager.getImageLoader().get(imagerUrl, listener);
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    /**
     * true:显示ListView,但隐藏GridView
     * false:显示GridView，但隐藏ListView
     */
    private boolean isListview = true;

    public void switchListAndGrid(ImageButton ib_list_grid) {
        if (isListview) {
            isListview = false;
            //显示GridView
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new PhotosDetailPagerAdapter());
            gridView.setOnItemClickListener(new PhotosDetailPagerListViewOnItemClickListener());
            //隐藏ListView
            listView.setVisibility(View.GONE);
            //按钮状态--ListView
            ib_list_grid.setImageResource(R.drawable.icon_pic_list_type);

        } else {
            isListview = true;
            //显示ListView
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new PhotosDetailPagerAdapter());
            listView.setOnItemClickListener(new PhotosDetailPagerListViewOnItemClickListener());
            //隐藏GridView
            gridView.setVisibility(View.GONE);
            //按钮状态--GridView
            ib_list_grid.setImageResource(R.drawable.icon_pic_grid_type);

        }
    }

    class PhotosDetailPagerListViewOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            LogUtil.e("ListView点击事件");
            PhotosDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            String largeimage = newsBean.getLargeimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("BigImageUrl", largeimage);
            context.startActivity(intent);

        }
    }
}
