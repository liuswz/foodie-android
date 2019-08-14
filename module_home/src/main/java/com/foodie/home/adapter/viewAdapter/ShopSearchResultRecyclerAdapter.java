package com.foodie.home.adapter.viewAdapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.SearchShop;
import com.foodie.base.entity.SimpleDish;
import com.foodie.home.R;
import com.foodie.home.bindingParam.ShopSearchClickCallBack;
import com.foodie.home.databinding.SearchResultItemBinding;
import com.foodie.home.databinding.SimpleDishBinding;
import com.foodie.home.myview.MyLinearLayout;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ShopSearchResultRecyclerAdapter extends RecyclerView.Adapter<ShopSearchResultRecyclerAdapter.MyViewHolder> {

    private final ShopSearchClickCallBack ShopSearchClickCallBack;
    private List<SearchShop> shopList;
   // private Context context;
    public ShopSearchResultRecyclerAdapter(ShopSearchClickCallBack ShopSearchClickCallBack) {
        this.ShopSearchClickCallBack = ShopSearchClickCallBack;
        //this.context = context.getApplicationContext();//防止内存泄漏
        shopList=new ArrayList<>();
    }
    public void setData(List<SearchShop> list){
        shopList=list;
        notifyDataSetChanged();
    }
    public void addData(List<SearchShop> list){
        shopList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchResultItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.search_result_item,
                        parent, false);

        binding.setCallback(ShopSearchClickCallBack);// 双向绑定；自动生成一个setXXX方法
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(shopList.size()!=0){
            holder.getBinding().setShop(shopList.get(position));
            holder.getBinding().executePendingBindings();
            FlowLayout flowlayout = holder.getBinding().moneyoffFlowlayout;
            flowlayout.removeAllViewsInLayout();
            if(shopList.get(position).getFullNum()==null){
                flowlayout=null;
            }else{
                String fullNums[] = shopList.get(position).getFullNum().split(",");
                String minusNums[] = shopList.get(position).getMinusNum().split(",");

                for(int i=0;i<fullNums.length;i++){
                    TextView tv = new TextView(flowlayout.getContext());
                    tv.setText("满"+fullNums[i]+"减"+minusNums[i]);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                    tv.setPadding(2,2,2,2);
                    tv.setBackgroundResource(R.drawable.tv_bg_shape);
                    flowlayout.addView(tv);
                }

            }
            FlowLayout dishFlowlayout = holder.getBinding().dishFlowlayout;
            dishFlowlayout.removeAllViewsInLayout();
            List<SimpleDish> dishList = shopList.get(position).getDishList();
            if(dishList==null){
                dishFlowlayout=null;
            }else{
                for(SimpleDish dish:dishList){
                    MyLinearLayout myLinearLayout = new MyLinearLayout(dishFlowlayout.getContext(),dish);

                    dishFlowlayout.addView(myLinearLayout);
                }

            }



        }

    }

    @Override
    public int getItemCount() {
        return shopList == null ? 0 : shopList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        SearchResultItemBinding searchResultItemBinding;

        public MyViewHolder(SearchResultItemBinding searchResultItemBinding) {
            super(searchResultItemBinding.getRoot());
            this.searchResultItemBinding = searchResultItemBinding;
        }

        public SearchResultItemBinding getBinding() {
            return searchResultItemBinding;
        }

        public void setBinding(SearchResultItemBinding searchResultItemBinding) {
            this.searchResultItemBinding = searchResultItemBinding;
        }
    }
}