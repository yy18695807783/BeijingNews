package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.NewsCenterPagerBean2;

import java.util.List;

/**
 * Created by 颜银 on 2016/10/31.
 * QQ:443098360
 * 微信：y443098360
 * 作用：左侧菜单的适配器
 */
public class LeftMenuFragmentAdpater extends BaseAdapter {

    private final Context context;
    private final List<NewsCenterPagerBean2.DataBean> leftdata;
    private int selectPosition;

    public LeftMenuFragmentAdpater(Context context,List<NewsCenterPagerBean2.DataBean> leftdata) {
        this.context = context;
        this.leftdata = leftdata;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return leftdata.size();
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

        TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu, null);
        textView.setText(leftdata.get(position).getTitle());
//            if(position ==selectPosition){
//                textView.setEnabled(true);
//            }else{
//                textView.setEnabled(false);
//            }

        textView.setEnabled(position == selectPosition);
        return textView;
    }
}