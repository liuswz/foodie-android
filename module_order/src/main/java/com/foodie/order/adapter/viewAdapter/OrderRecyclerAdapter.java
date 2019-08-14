package com.foodie.order.adapter.viewAdapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.SimpleOrder;

import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.enums.GoodGetWayType;
import com.foodie.base.enums.GoodReachStatus;
import com.foodie.base.enums.GoodType;
import com.foodie.base.enums.OrderFinishStatus;
import com.foodie.order.R;
import com.foodie.order.bindingParam.OrderClickByIdCallBack;
import com.foodie.order.bindingParam.OrderClickCallBack;
import com.foodie.order.databinding.OrderItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.MyViewHolder> {

    private  OrderClickByIdCallBack checkOrderDetail;
    private  OrderClickByIdCallBack finishOrder;
    private  OrderClickCallBack commentOrder;
    private OrderClickCallBack payOrder;
    private List<SimpleOrder> orderList;

    public OrderRecyclerAdapter(OrderClickByIdCallBack checkOrderDetail,OrderClickByIdCallBack finishOrder,OrderClickCallBack commentOrder,OrderClickCallBack payOrder) {
        this.checkOrderDetail = checkOrderDetail;
        this.finishOrder = finishOrder;
        this.commentOrder = commentOrder;
        this.payOrder = payOrder;
        orderList=new ArrayList<>();
    }
    public void setData(List<SimpleOrder> list){
        orderList=list;
        notifyDataSetChanged();
    }
    public void addData(List<SimpleOrder> list){
        orderList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.order_item,
                        parent, false);

        binding.setCheckOrderDetail(checkOrderDetail);// 双向绑定；自动生成一个setXXX方法
        binding.setCommentOrder(commentOrder);
        binding.setFinishOrder(finishOrder);
        binding.setPayOrder(payOrder);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(orderList.size()!=0){
            OrderItemBinding binding = holder.getBinding();
            SimpleOrder order = orderList.get(position);
            binding.setOrder(order);
            //switch无法用枚举
            switch (order.getGetWay()){
                case 0:
                    binding.setOrderStatus2(GoodGetWayType.GoToShop.getName());
                    break;
                case 1:
                    binding.setOrderStatus2(GoodGetWayType.Appoint.getName());
                    break;
                case 2:
                    binding.setOrderStatus2(GoodGetWayType.Delivery.getName());
                    break;
                case 3:
                    binding.setOrderStatus2(GoodGetWayType.GetInShop.getName());
                    break;
            }
            if(order.getIfFinish()== OrderFinishStatus.HadFinish.getIndex()){
                binding.setOrderStatus1("订单已完成");
            }else if(order.getIfGoodHadReach()!=null&&order.getIfGoodHadReach()== GoodReachStatus.HadReach.getIndex()&&order.getIfFinish()== OrderFinishStatus.NotFinish.getIndex()){
                binding.setOrderStatus1("已到货，可以去取啦");
            }else if(order.getIfGoodHadReach()!=null&&order.getIfGoodHadReach()== GoodReachStatus.NotReach.getIndex()){
                binding.setOrderStatus1("未到货");
            }else{
                binding.setOrderStatus1("订单正在进行");
            }
            if(order.getGoodType()== GoodType.Product.getIndex()&&order.getIfFinish()==OrderFinishStatus.NotFinish.getIndex()){
                binding.setFinishVisibility(true);
            }else{
                binding.setFinishVisibility(false);
            }
            binding.executePendingBindings();


        }

    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        OrderItemBinding orderItemBinding;

        public MyViewHolder(OrderItemBinding orderItemBinding) {
            super(orderItemBinding.getRoot());
            this.orderItemBinding = orderItemBinding;
        }

        public OrderItemBinding getBinding() {
            return orderItemBinding;
        }

        public void setBinding(OrderItemBinding orderItemBinding) {
            this.orderItemBinding = orderItemBinding;
        }
    }
}