package com.foodie.order.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.enums.GetOrderFlag;
import com.foodie.base.enums.GoodType;
import com.foodie.base.enums.PayWay;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.router.RouterFragmentPath;
import com.foodie.order.R;
import com.foodie.order.adapter.viewAdapter.OrderRecyclerAdapter;
import com.foodie.order.bindingParam.OrderClickByIdCallBack;
import com.foodie.order.bindingParam.OrderClickCallBack;
import com.foodie.order.databinding.FragmentOrderBinding;
import com.foodie.order.ui.CommentActivity;
import com.foodie.order.ui.CommentChooseProductActivity;
import com.foodie.order.ui.OrderDetailActivity;
import com.foodie.order.ui.viewmodel.OrderViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import java.util.List;


/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Order.PAGER_ORDER)
public class OrderFragment  extends Fragment {
    private FragmentOrderBinding fragmentOrderBinding;
    private OrderRecyclerAdapter orderRecyclerAdapter;
    private XRecyclerView xRecyclerView;
    private OrderViewModel viewModel;
    private Integer page = 1;
    //是否加载更多标志位
    private boolean showMoreFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentOrderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        fragmentOrderBinding.allOrder.setOnClickListener(allOrder);
        fragmentOrderBinding.notpayOrder.setOnClickListener(notPayOrder);
        fragmentOrderBinding.hadpayOrder.setOnClickListener(hadPayOrder);
        fragmentOrderBinding.appointOrder.setOnClickListener(appointOrder);

        //初始化定位
        return fragmentOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        fragmentHomeBinding.setListenerByLocation(listenerByLocation);
//        fragmentHomeBinding.setListenerByMark(listenerByMark);
        viewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        Integer userId=1;
        viewModel.setUserId(userId);

        initXRecyclerView();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.loadAllSimpleOrder(page);
        observeViewModelOrderList(viewModel);
        observeViewModelFinishResponse(viewModel);
        observeViewModelPayResponse(viewModel);
    }
    private void initXRecyclerView() {
        orderRecyclerAdapter = new OrderRecyclerAdapter(checkOrderDetail,finishOrder,commentOrder,payOrder);
//        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView = fragmentOrderBinding.recyclerview;
        xRecyclerView.setAdapter(orderRecyclerAdapter);
        xRecyclerView.setLayoutManager(mLayoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                if(viewModel.getTotal()>page){
                    showMoreFlag=true;
                    viewModel.loadAllSimpleOrder(++page);

                }else{
                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });
    }
    private OrderClickByIdCallBack checkOrderDetail=new OrderClickByIdCallBack() {
        @Override
        public void onClick(Integer orderId, View view) {
            Intent intent  =new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtra("id",orderId+"");
            startActivity(intent);
        }
    };
    private  OrderClickByIdCallBack finishOrder = new OrderClickByIdCallBack() {
        @Override
        public void onClick(Integer orderId, View view) {
            viewModel.updateFinishStatus(orderId);
        }
    };
    private  OrderClickCallBack commentOrder = new OrderClickCallBack() {
        @Override
        public void onClick(SimpleOrder order, View view) {
            if(order.getGoodType()== GoodType.Dish.getIndex()){
                Intent intent  =new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("orderId",order.getId()+"");
                intent.putExtra("goodId",order.getGoodId()+"");
                intent.putExtra("goodType",order.getGoodType()+"");
                startActivity(intent);
            }else{
                Intent intent  =new Intent(getActivity(), CommentChooseProductActivity.class);
                intent.putExtra("orderId",order.getId()+"");
                startActivity(intent);
            }

        }
    };
    private OrderClickCallBack payOrder =new OrderClickCallBack() {
        @Override
        public void onClick(final SimpleOrder order, View view) {
            new TDialog.Builder(getFragmentManager())
                    .setLayoutRes(R.layout.dialog_pay_way)
                    .setScreenWidthAspect(getActivity(), 1.0f)
                    .setGravity(Gravity.BOTTOM)
                    .addOnClickListener(R.id.tv_wechat, R.id.tv_alipay)
                    .setOnViewClickListener(new OnViewClickListener() {
                        @Override
                        public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                            switch (view.getId()) {
                                case R.id.tv_wechat:
                                    viewModel.updatePayStatus(order.getId(),PayWay.WeChat.getIndex());
                                    tDialog.dismiss();
                                    break;
                                case R.id.tv_alipay:
                                    viewModel.updatePayStatus(order.getId(),PayWay.AliPay.getIndex());
                                    tDialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .create()
                    .show();
        }
    };

    private OnClickEvent allOrder = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            page=1;
            showMoreFlag=false;
            viewModel.setFlag(GetOrderFlag.AllOrder.getIndex());
            viewModel.loadAllSimpleOrder(page);
        }
    };
    private OnClickEvent notPayOrder = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            page=1;
            showMoreFlag=false;
            viewModel.setFlag(GetOrderFlag.NotPay.getIndex());
            viewModel.loadAllSimpleOrder(page);
        }
    };
    private OnClickEvent hadPayOrder = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            page=1;
            showMoreFlag=false;
            viewModel.setFlag(GetOrderFlag.HadPay.getIndex());
            viewModel.loadAllSimpleOrder(page);
        }
    };
    private OnClickEvent appointOrder = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            page=1;
            showMoreFlag=false;
            viewModel.setFlag(GetOrderFlag.Appoint.getIndex());
            viewModel.loadAllSimpleOrder(page);
        }
    };
    private void observeViewModelOrderList(final OrderViewModel viewModel) {

        viewModel.getOrderList().observe(this, new Observer<List<SimpleOrder>>() {
            @Override
            public void onChanged(List<SimpleOrder> orderList) {
                if(orderList==null||orderList.size()==0){
                    fragmentOrderBinding.setEmptyVisibility(false);
                }else{
                    fragmentOrderBinding.setEmptyVisibility(true);
                }
                if (showMoreFlag) {
                    orderRecyclerAdapter.addData(orderList);
                    xRecyclerView.loadMoreComplete();

                } else {
                    orderRecyclerAdapter.setData(orderList);
                    xRecyclerView.loadMoreComplete();
                }

            }
        });
    }

    private void observeViewModelPayResponse(final OrderViewModel viewModel) {

        viewModel.getPayResponse().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer flag) {
                if(flag== ResultCode.SUCCESS.getIndex()){
                    Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    viewModel.loadAllSimpleOrder(page);
                }

            }
        });
    }
    private void observeViewModelFinishResponse(final OrderViewModel viewModel) {

        viewModel.getFinishResponse().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer flag) {
                if(flag== ResultCode.SUCCESS.getIndex()){
                    Toast.makeText(getActivity(), "确定收货成功成功", Toast.LENGTH_SHORT).show();
                    viewModel.loadAllSimpleOrder(page);
                }

            }
        });
    }

}