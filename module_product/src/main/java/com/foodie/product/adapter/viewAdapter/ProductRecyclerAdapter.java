package com.foodie.product.adapter.viewAdapter;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.foodie.base.entity.Product;
import java.util.ArrayList;
import java.util.List;
import com.foodie.product.R;
import com.foodie.product.bindingParam.ProductClickCallBack;
import com.foodie.product.databinding.ProductItemBinding;
import com.nex3z.flowlayout.FlowLayout;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder> {

    private final ProductClickCallBack productClickCallBack;
    private List<Product> productList;
    private ProductClickCallBack addProductCallBack;
    private ProductClickCallBack minusProductCallBack;
    private LifecycleOwner owner;
    // private Context context;
    public ProductRecyclerAdapter(ProductClickCallBack productClickCallBack,ProductClickCallBack addProductCallBack,ProductClickCallBack minusProductCallBack,LifecycleOwner owner) {
        this.productClickCallBack = productClickCallBack;
        this.addProductCallBack = addProductCallBack;
        this.minusProductCallBack = minusProductCallBack;
        this.owner = owner;
        //this.context = context.getApplicationContext();//防止内存泄漏
        productList=new ArrayList<>();
    }
    public void setData(List<Product> list){
        productList=list;
        notifyDataSetChanged();
    }
    public void addData(List<Product> list){
        productList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item,
                        parent, false);



        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(productList.size()!=0){
            ProductItemBinding binding= holder.getBinding();

            if(productList.get(position).getNum()==null){
                productList.get(position).setNum(new MutableLiveData<Integer>());
                productList.get(position).getNum().setValue(0);
                binding.setMinusVisibility(false);
            }else{
                binding.setMinusVisibility(true);
            }


            productList.get(position).getNum().observe(owner, new Observer<Integer>() {
                @Override
                public void onChanged(Integer num) {

                    if(num==0){
                        binding.setMinusVisibility(false);
                        binding.setProduct(productList.get(position));
                    }else {
                        binding.setMinusVisibility(true);
                        binding.setProduct(productList.get(position));
                    }
                }
            });

            binding.setProduct(productList.get(position));
            binding.setAddGoodCallback(addProductCallBack);
            binding.setMinusGoodCallback(minusProductCallBack);
            binding.setGetGoodDetailCallback(productClickCallBack);// 双向绑定；自动生成一个setXXX方法

            FlowLayout flowlayout = holder.getBinding().moneyoffFlowlayout;
            flowlayout.removeAllViewsInLayout();
            if(productList.get(position).getFullNum()==null){
                flowlayout=null;
            }else{
                String fullNums[] = productList.get(position).getFullNum().split(",");
                String minusNums[] = productList.get(position).getMinusNum().split(",");

                for(int i=0;i<fullNums.length;i++){
                    TextView tv = new TextView(flowlayout.getContext());
                    tv.setText("满"+fullNums[i]+"减"+minusNums[i]);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,5);
                    tv.setPadding(2,2,2,2);
                    tv.setBackgroundResource(R.drawable.tv_bg_shape);
                    flowlayout.addView(tv);
                }

            }
            holder.getBinding().executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        ProductItemBinding productItemBinding;

        public MyViewHolder(ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());
            this.productItemBinding = productItemBinding;
        }

        public ProductItemBinding getBinding() {
            return productItemBinding;
        }

        public void setBinding(ProductItemBinding productItemBinding) {
            this.productItemBinding = productItemBinding;
        }
    }
}