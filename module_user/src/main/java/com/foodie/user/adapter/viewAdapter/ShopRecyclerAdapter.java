package com.foodie.user.adapter.viewAdapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.ShopDetail;


import com.foodie.user.R;
import com.foodie.user.bindingParam.CancelCollectClickBack;
import com.foodie.user.bindingParam.ShopClickCallBack;
import com.foodie.user.databinding.ShopItemBinding;
import com.foodie.user.ui.viewmodel.CollectionViewModel;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.MyViewHolder> {

    private final ShopClickCallBack shopClickCallBack;

    private List<ShopDetail> shopList;
    private CollectionViewModel collectionViewModel;
   // private Context context;
    public ShopRecyclerAdapter(ShopClickCallBack shopClickCallBack,CollectionViewModel collectionViewModel) {
        this.shopClickCallBack = shopClickCallBack;
        this.collectionViewModel = collectionViewModel;

        //this.context = context.getApplicationContext();//防止内存泄漏
        shopList=new ArrayList<>();
    }
    public void setData(List<ShopDetail> list){
        shopList=list;
        notifyDataSetChanged();
    }
    public void addData(List<ShopDetail> list){
        shopList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ShopItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.shop_item,
                        parent, false);

        binding.setCallback(shopClickCallBack);// 双向绑定；自动生成一个setXXX方法

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(shopList.size()!=0){
            holder.getBinding().setShop(shopList.get(position));
            holder.getBinding().setCancelCallback(new CancelCollectClickBack() {
                @Override
                public void onClick(Integer shopId, View view) {
                    collectionViewModel.deleteShopCollect(shopId);
                    shopList.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.getBinding().executePendingBindings();
//            FlowLayout flowlayout = holder.getBinding().moneyoffFlowlayout;
//            flowlayout.removeAllViewsInLayout();
//            if(shopList.get(position).getFullNum()==null){
//                flowlayout=null;
//            }else{
//                String fullNums[] = shopList.get(position).getFullNum().split(",");
//                String minusNums[] = shopList.get(position).getMinusNum().split(",");
//
//                for(int i=0;i<fullNums.length;i++){
//                    TextView tv = new TextView(flowlayout.getContext());
//                    tv.setText("满"+fullNums[i]+"减"+minusNums[i]);
//                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
//                    tv.setPadding(2,2,2,2);
//                    tv.setBackgroundResource(R.drawable.tv_bg_shape);
//                    flowlayout.addView(tv);
//                }
//
//            }
//            holder.getBinding().setCallback(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    RxView.clicks(view)
//                            .throttleFirst(1, TimeUnit.SECONDS)//1秒钟内只允许点击1次
//                            .subscribe(new Consumer<Object>() {
//                                @Override
//                                public void accept(Object object) throws Exception {
//                                    Toast.makeText(context, "111111111111111111", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        return shopList == null ? 0 : shopList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        ShopItemBinding shopItemBinding;

        public MyViewHolder(ShopItemBinding shopItemBinding) {
            super(shopItemBinding.getRoot());
            this.shopItemBinding = shopItemBinding;
        }

        public ShopItemBinding getBinding() {
            return shopItemBinding;
        }

        public void setBinding(ShopItemBinding shopItemBinding) {
            this.shopItemBinding = shopItemBinding;
        }
    }
}