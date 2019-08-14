package com.foodie.home.adapter.viewAdapter;

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

import com.foodie.base.entity.Dish;
import com.foodie.base.entity.ShopDetail;
import com.foodie.home.R;
import com.foodie.home.bindingParam.AddDishCallBack;
import com.foodie.home.bindingParam.ShopClickCallBack;
import com.foodie.home.databinding.DishItemBinding;
import com.foodie.home.databinding.ShopItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class DishRecyclerAdapter extends RecyclerView.Adapter<DishRecyclerAdapter.MyViewHolder> {

    private final AddDishCallBack getDishDetailCallback;
    private final AddDishCallBack addDishCallBack;
    private final AddDishCallBack minusDishCallBack;
    private LifecycleOwner owner;

    private List<Dish> dishList;
   // private Context context;
    public DishRecyclerAdapter(AddDishCallBack getDishDetailCallback, AddDishCallBack addDishCallBack, AddDishCallBack minusDishCallBack, LifecycleOwner owner) {
        this.getDishDetailCallback = getDishDetailCallback;
        this.addDishCallBack = addDishCallBack;
        this.minusDishCallBack = minusDishCallBack;
        this.owner = owner;
        //this.context = context.getApplicationContext();//防止内存泄漏
        dishList=new ArrayList<>();
    }
    public void setData(List<Dish> list){
        dishList=list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DishItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.dish_item,
                        parent, false);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(dishList.size()!=0){
            DishItemBinding dishItemBinding = holder.getBinding();

            if(dishList.get(position).getNum()==null){
                dishList.get(position).setNum(new MutableLiveData<Integer>());
                dishList.get(position).getNum().setValue(0);
                dishItemBinding.setMinusVisibility(false);
            }else{
                dishItemBinding.setMinusVisibility(true);
            }

            dishList.get(position).getNum().observe(owner, new Observer<Integer>() {
                @Override
                public void onChanged(Integer num) {

                    if(num==0){
                        dishItemBinding.setMinusVisibility(false);
                        dishItemBinding.setDish(dishList.get(position));
                    }else {
                        dishItemBinding.setMinusVisibility(true);
                        dishItemBinding.setDish(dishList.get(position));
                    }
                }
            });
            dishItemBinding.setDish(dishList.get(position));
            dishItemBinding.setAddDishCallback(addDishCallBack);
            dishItemBinding.setMinusDishCallback(minusDishCallBack);
            dishItemBinding.setGetDishDetailCallback(getDishDetailCallback);// 双向绑定；自动生成一个setXXX方法
            dishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return dishList == null ? 0 : dishList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        DishItemBinding dishItemBinding;

        public MyViewHolder(DishItemBinding dishItemBinding) {
            super(dishItemBinding.getRoot());
            this.dishItemBinding = dishItemBinding;
        }

        public DishItemBinding getBinding() {
            return dishItemBinding;
        }

        public void setBinding(DishItemBinding dishItemBinding) {
            this.dishItemBinding = dishItemBinding;
        }
    }
}