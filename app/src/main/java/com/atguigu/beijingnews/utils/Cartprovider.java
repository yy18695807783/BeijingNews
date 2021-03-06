package com.atguigu.beijingnews.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.Toast;

import com.atguigu.beijingnews.domain.ShoppingCart;
import com.atguigu.beijingnews.domain.ShoppingPagerBean;
import com.atguigu.beijingnews.view.AddSubView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/10/26.
 * QQ:443098360
 * 微信：y443098360
 * 作用：购物车存储器类，存取购物车的内容；取内容json数据-->List; 存List-->json数据
 */
public class Cartprovider {

    public static final String JSON_CART = "json_cart";
    private final Context context;
    private  int maxValue;
    private AddSubView addSubView;

    /**
     * SparseArray代替HashMap，性能更佳
     */
    private SparseArray<ShoppingCart> sparseArray;
    private List<ShoppingCart> allData;
    private List<ShoppingCart> localData;

    public Cartprovider(Context context) {
        this.context = context;
        //默认创建10个实例
        sparseArray = new SparseArray<>(10);
        listToSparse();
        addSubView = new AddSubView(context);
        maxValue = addSubView.getMaxValue();
    }


    /**
     * 从列表中得到数据并赋值给sparseArray
     */
    private void listToSparse() {
        List<ShoppingCart> shoppingCarts = getAllData();
        for (int i = 0; i < shoppingCarts.size(); i++) {
            ShoppingCart shoppingCart = shoppingCarts.get(i);
            sparseArray.put(shoppingCart.getId(), shoppingCart);
        }
    }

    /**
     * 得到所有数据
     *
     * @return
     */
    public List<ShoppingCart> getAllData() {
        return getLocalData();
    }

    /**
     * 得到缓存数据
     *
     * @return
     */
    public List<ShoppingCart> getLocalData() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        //从本地内存中获取数据
        String json = CacheUtils.getString(context, JSON_CART);
        if (!TextUtils.isEmpty(json)) {
            //把json数据解析成集合，并返回
            shoppingCarts = new Gson().fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return shoppingCarts;
    }

    /**
     * 添加数据
     *
     * @param cart
     */
    public void addDataToCart(ShoppingCart cart) {
        //1、增加数据 并判断是否存在
        ShoppingCart temp = sparseArray.get(cart.getId());
        if (temp != null) {
            //列表中存在
            LogUtil.e("---------maxValue = " + maxValue);
            if(temp.getCount() < maxValue){
                temp.setCount(temp.getCount() + 1);
            }else {
                Toast.makeText(context, "加入失败!!没有更多商品啦!!", Toast.LENGTH_SHORT).show();
            }

        } else {
            //列表中不存在
            temp = cart;
            temp.setCount(1);
        }

//            LogUtil.e("每个信息,ShoppingCart cart = " + cart.toString() +",name = " + cart.getName());
        //保存到sparseArray  也表示更新
        sparseArray.put(temp.getId(), temp);

        //保存到本地
        commit();
    }

    /**
     * 删除数据
     *
     * @param cart
     */
    public void deleteDataToCart(ShoppingCart cart) {
        //直接删除数据
        sparseArray.delete(cart.getId());
        //保存到本地
        commit();
    }

    /**
     * 修改数据
     *
     * @param cart
     */
    public void updataToCart(ShoppingCart cart) {
        //修改数据
        sparseArray.put(cart.getId(), cart);
        //保存到本地
        commit();
    }

    /**
     * 把List列表数据-->json文本数据-->保存到本地
     */
    private void commit() {
        //从sparseArray中得到数据
        List<ShoppingCart> shoppingCarts = sparseToList();
        String savejson = new Gson().toJson(shoppingCarts);
        //保存打本地内存中
        CacheUtils.putString(context, JSON_CART, savejson);

    }

    private List<ShoppingCart> sparseToList() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            ShoppingCart shoppingCart = sparseArray.valueAt(i);
            shoppingCarts.add(shoppingCart);
        }
        return shoppingCarts;
    }

    /**
     * 把商品转换成购物车中物品  并返回数据
     *
     * @param wares
     * @return
     */
    public ShoppingCart conversion(ShoppingPagerBean.Wares wares) {
        ShoppingCart shoppingCart = new ShoppingCart();
//        shoppingCart.setCount(shoppingCart.getCount());
//        shoppingCart.setIsCheck(true);
        shoppingCart.setId(wares.getId());
        shoppingCart.setName(wares.getName());
        shoppingCart.setImgUrl(wares.getImgUrl());
        shoppingCart.setDescription(wares.getDescription());
        shoppingCart.setPrice(wares.getPrice());
        shoppingCart.setSale(wares.getSale());
        return shoppingCart;
    }

}
