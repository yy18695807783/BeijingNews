package com.atguigu.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> datas;

    public RecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    /**
     * 初始化视图的方法
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.item_recycle_view, null);
        return new ViewHolder(itemView);
    }

    /**
     * 装配数据的方法
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = datas.get(position);
        holder.tv_title.setText(data);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    /**
     * 添加集合信息
     * @param position
     * @param data
     */
    public void addData(int position, String data) {
        datas.add(position,data);
        //添加信息的刷新方法
        notifyItemInserted(position);
    }

    /**
     * 删除集合数据方法
     * @param position
     */
    public void deleteData(int position) {
        datas.remove(position);
        //刷新删除信息的方法
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_icon;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
