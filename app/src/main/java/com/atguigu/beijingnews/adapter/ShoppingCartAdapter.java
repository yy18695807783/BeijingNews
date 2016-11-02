package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.ShoppingCart;
import com.atguigu.beijingnews.utils.Cartprovider;
import com.atguigu.beijingnews.view.AddSubView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/26.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final Context context;
    private List<ShoppingCart> datas;
    /**
     * 全选框
     */
    private CheckBox cb_all;
    private Cartprovider cartprovider;
    /**
     * 显示总价格的生明
     */
    private TextView tv_price_all;

    public ShoppingCartAdapter(Context context, List<ShoppingCart> shoppingCarts, TextView tv_price_all, final CheckBox cb_all) {
        this.context = context;
        this.datas = shoppingCarts;
        this.tv_price_all = tv_price_all;
        this.cb_all = cb_all;
        cartprovider = new Cartprovider(context);
        //显示总价格
        showTotalPrice();
        //设置item的监听
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //1.得到对应位置的对象
                ShoppingCart cart = datas.get(position);
                //2.勾选状态取反
                cart.setIsCheck(!cart.isCheck());//取反  没有勾上价格不会加上
                //3.状态刷新
                notifyItemChanged(position);
                //4.校验全选和非全选
                checkAll();
                //5.显示总价格
                showTotalPrice();
                //6.保存到内存那种
                cartprovider.updataToCart(datas.get(position));
            }
        });
        checkAll();

        //全选按钮的 设置点击事件_  是否全选
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.得到它是否选中的状态
                boolean isCheck = cb_all.isChecked();
                //2.设置全选和非全选
                checkAll_none(isCheck);
                //3.重新计算总价格
                showTotalPrice();
            }
        });
    }

    /**
     * 设置全选和非全选
     *
     * @param isCheck
     */
    public void checkAll_none(boolean isCheck) {
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                ShoppingCart cart = datas.get(i);
                cart.setIsCheck(isCheck);
                notifyItemChanged(i);
            }
        }
    }

    /**
     * 验证是否是全选
     */
    public void checkAll() {
        int number = 0;
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                ShoppingCart cart = datas.get(i);
                if (!cart.isCheck()) {//只要有一个没有被选中，就是非全选
                    //没有选中
                    cb_all.setChecked(false);//非勾选
                } else {
                    //选中
                    number++;
                }
            }

            if (number == datas.size()) {//选中的个数和集合总数相同
                cb_all.setChecked(true);//勾选

//                if(number == 0){
//                    cb_all.setChecked(false);//勾选
//                }

            } else {
//                cb_all.setChecked(false);//非勾选
            }

        } else {
//            cb_all.setChecked(false);//非勾选
            //用于处理删除后没有商品时，全选不勾选
            cb_all.setChecked(false);//勾选
        }
    }

    /**
     * 删除选中的数据
     */
    public void deleteData() {
//        if(datas != null && datas.size() >0){
//            for(int i=0;i<datas.size();i++){
//                ShoppingCart cart = datas.get(i);
//                if(cart.isCheck()){
//                    //1.删除本地缓存的
//                    cartProvider.deleteData(cart);
//
//                    //2.删除当前内存的
//                    datas.remove(cart);
//
//                    //3.刷新数据
//                    notifyItemRemoved(i);
//
//                    i--;
//                }
//            }
//        }

        //用迭代器
//        if(datas != null && datas.size() >0){
//            for(Iterator iterator = datas.iterator();iterator.hasNext();){
//
//                ShoppingCart cart = (ShoppingCart) iterator.next();
//
//                if(cart.isCheck()){
//
//                    //这行代码放在前面
//                    int position = datas.indexOf(cart);
//
//                    //1.删除本地缓存的
//                    cartprovider.deleteDataToCart(cart);
//
//                    //2.删除当前内存的
////                    datas.remove(cart);
//                    iterator.remove();//使用的是iterator的删除
//
//                    //3.刷新数据
//                    notifyItemRemoved(position);
//
//                }
//            }
//        }
        //while迭代器
        Iterator iterator = datas.iterator();
        while (iterator.hasNext()) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isCheck()) {

                //这行代码放在前面
                int position = datas.indexOf(cart);

                //1.删除本地缓存的
                cartprovider.deleteDataToCart(cart);

                //2.删除当前内存的
//                    datas.remove(cart);
                iterator.remove();//使用的是iterator的删除

                //3.刷新数据
                notifyItemRemoved(position);

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_shopping_cart_pager, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShoppingCart shoppingCart = datas.get(position);
        Glide.with(context)
                .load(shoppingCart.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .into(holder.iv_icon);
        holder.tv_name.setText(shoppingCart.getName());
        holder.cb_checkbox.setChecked(shoppingCart.isCheck());
        holder.tv_price.setText("￥" + shoppingCart.getPrice());
        holder.addSubView.setValue(shoppingCart.getCount());
        holder.addSubView.setOnButtonSubAddChangeListener(new AddSubView.OnButtonSubAddChangeListener() {
            @Override
            public void onButtonSubChange(View v, int value) {
                //减小  value即为更新后的数
                shoppingCart.setCount(value);
                cartprovider.updataToCart(shoppingCart);
                //重新显示价格
                showTotalPrice();
            }

            @Override
            public void onButtonAddChange(View v, int value) {
                //增大
                shoppingCart.setCount(value);
                cartprovider.updataToCart(shoppingCart);
                //重新显示价格
                showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb_checkbox;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private AddSubView addSubView;

        public ViewHolder(View itemView) {
            super(itemView);
            cb_checkbox = (CheckBox) itemView.findViewById(R.id.cb_checkbox);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            addSubView = (AddSubView) itemView.findViewById(R.id.addSubView);

            //设置点击某条的监听
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition());
                    }

                }
            });


        }
    }

    /**
     * 显示总价格的方法
     */
    public void showTotalPrice() {
        tv_price_all.setText("合计：￥ " + getTotalPrice());
    }

    /**
     * 得到总价格
     *
     * @return
     */
    public double getTotalPrice() {
        double totalPrice = 0;
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                ShoppingCart shoppingCart = datas.get(i);
                //只用选择的参与计算
                if (shoppingCart.isCheck()) {
                    totalPrice += shoppingCart.getPrice() * shoppingCart.getCount();
                }
            }
        }
        return totalPrice;
    }

    /**
     * 点击某个的监听
     */
    public interface OnItemClickListener {
        /**
         * 当某个item被点击的时候回调
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    /**
     * 设置某条的监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
