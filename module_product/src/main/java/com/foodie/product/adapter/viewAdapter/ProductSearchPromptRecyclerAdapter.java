package com.foodie.product.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.ShopSearchPrompt;
import com.foodie.product.R;
import com.foodie.product.bindingParam.ProductSearchPromptClickCallBack;
import com.foodie.product.databinding.SearchPromptItemBinding;


import java.util.ArrayList;
import java.util.List;

public class ProductSearchPromptRecyclerAdapter extends RecyclerView.Adapter<ProductSearchPromptRecyclerAdapter.MyViewHolder> {

    private final ProductSearchPromptClickCallBack productSearchPromptClickCallBack;
    private List<String> searchPromptsList;
   // private Context context;
    public ProductSearchPromptRecyclerAdapter(ProductSearchPromptClickCallBack productSearchPromptClickCallBack) {
        this.productSearchPromptClickCallBack = productSearchPromptClickCallBack;
        //this.context = context.getApplicationContext();//防止内存泄漏
        searchPromptsList=new ArrayList<>();
    }
  //  DiffUtil方式 但有问题
//    public void setData(List<ShopSearchPrompt> list) {
//        if (this.searchPromptsList == null) { //新增
//            this.searchPromptsList = list;
//            searchPromptsList=list;
//            notifyItemRangeInserted(0, searchPromptsList.size());
//        } else {
//            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//                @Override
//                public int getOldListSize() { //旧大小
//                    return ShopSearchPromptRecyclerAdapter.this.searchPromptsList.size();
//                }
//
//                @Override
//                public int getNewListSize() {//新大小
//                    return list.size();
//                }
//
//                @Override
//                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {//新旧的item是否相同
//                    return ShopSearchPromptRecyclerAdapter.this.searchPromptsList.get(oldItemPosition).getShopName().
//                            equals(list.get(newItemPosition).getShopName());
//                }
//
//                @Override
//                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {// 新旧的内容是否相同
//                    ShopSearchPrompt shopSearchPrompt = list.get(newItemPosition);
//                    ShopSearchPrompt old = ShopSearchPromptRecyclerAdapter.this.searchPromptsList.get(oldItemPosition);
//                    return shopSearchPrompt.getShopName().equals(old.getShopName());
//
//                }
//            });
//            this.searchPromptsList = list;
//            result.dispatchUpdatesTo(this);
//        }
//    }
    public void setData(List<String> list){
        this.searchPromptsList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchPromptItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.search_prompt_item,
                        parent, false);
        binding.setCallback(productSearchPromptClickCallBack);// 双向绑定；自动生成一个setXXX方法
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(searchPromptsList.size()!=0){
            holder.getBinding().setValue(searchPromptsList.get(position));
            holder.getBinding().executePendingBindings();
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
        return searchPromptsList == null ? 0 : searchPromptsList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        SearchPromptItemBinding shopItemBinding;

        public MyViewHolder(SearchPromptItemBinding shopItemBinding) {
            super(shopItemBinding.getRoot());
            this.shopItemBinding = shopItemBinding;
        }

        public SearchPromptItemBinding getBinding() {
            return shopItemBinding;
        }

        public void setBinding(SearchPromptItemBinding shopItemBinding) {
            this.shopItemBinding = shopItemBinding;
        }
    }
}