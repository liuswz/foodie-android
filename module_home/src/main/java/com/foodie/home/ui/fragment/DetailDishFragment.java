package com.foodie.home.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishType;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.DishRecyclerAdapter;
import com.foodie.home.adapter.viewAdapter.DishTypeListAdapter;

import com.foodie.home.adapter.viewAdapter.ShopCarRecyclerAdapter;
import com.foodie.home.bindingParam.AddDishCallBack;
import com.foodie.home.bindingParam.AddHadAddDishCallBack;
import com.foodie.home.bindingParam.ShopTypeClickCallBack;
import com.foodie.home.databinding.DishDialogBinding;
import com.foodie.home.databinding.FragmentDetailDishBinding;
import com.foodie.home.myview.DishDialog;
import com.foodie.home.myview.HotDishLinearLayout;
import com.foodie.home.myview.ShopCarDialog;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nex3z.flowlayout.FlowLayout;


import java.util.List;
import java.util.Map;

public class DetailDishFragment extends Fragment implements DialogInterface.OnDismissListener{

    private FragmentDetailDishBinding fragmentDishBinding;
    private DetailViewModel viewModel;

    private DishRecyclerAdapter dishRecyclerAdapter;

 //   private DishTypeRecyclerAdapter dishTypeRecyclerAdapter;
    private DishTypeListAdapter dishTypeListAdapter;
    private XRecyclerView xRecyclerView;
//    private RecyclerView recyclerView;
    private ListView listView;
    private DishDialog dishDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentDishBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_dish, container, false);

        initXRecyclerView();
        return fragmentDishBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        viewModel.loadHotDishList();
        viewModel.loadDishTypeList();
        observeViewModelHotDishList(viewModel);
        //observeViewModelHadAddDishMap(viewModel);
        observeViewModelDishTypeList(viewModel);
        observeViewModelDishList(viewModel);

    }
    private void initXRecyclerView() {


        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        dishRecyclerAdapter = new DishRecyclerAdapter(getDishDetailCallBack,addDishCallBack,minusDishCallBack,this.getViewLifecycleOwner());
        xRecyclerView=fragmentDishBinding.dishXrecyclerview;
        xRecyclerView.setAdapter(dishRecyclerAdapter);
        xRecyclerView.setLayoutManager(mLayoutManager2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);




    }
    private void initFlowLayout(List<Dish> dishList){
        FlowLayout dishFlowlayout = fragmentDishBinding.dishFlowlayout;
        dishFlowlayout.removeAllViewsInLayout();
        if(dishList==null){
            dishFlowlayout=null;
        }else{
            for(Dish dish:dishList){
                HotDishLinearLayout hotDishLinearLayout = new HotDishLinearLayout(dishFlowlayout.getContext(),dish,addDishCallBack,minusDishCallBack,getDishDetailCallBack,this.getViewLifecycleOwner());
                dishFlowlayout.addView(hotDishLinearLayout);
            }
        }
    }


//    private ShopTypeClickCallBack shopTypeClickCallBack = new ShopTypeClickCallBack() {
//
//        @Override
//        public void onClick(String typeName, View view) {
//
//
//
//        }
//    };
    private static long mLastClickTime0 = 0;
    public static final long TIME_INTERVAL0 = 400L;
    private AddDishCallBack addDishCallBack = new AddDishCallBack() {
        @Override
        public void onClick(Dish dish,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {

                Integer num = dish.getNum().getValue();
                dish.getNum().setValue((num+1));
                viewModel.addDishMap(dish);

                mLastClickTime0 = nowTime;
            }

          //  viewModel.addSumPrice(dish.getPrice());
        }
    };


    private AddDishCallBack minusDishCallBack = new AddDishCallBack() {
        @Override
        public void onClick(Dish dish,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {

                Integer num = dish.getNum().getValue();
                if (num > 0) {
                    dish.getNum().setValue((num - 1) );
                }
                viewModel.minusDishMap(dish);
                mLastClickTime0 = nowTime;
            }

         //   viewModel.minusSumPrice(dish.getPrice());
        }
    };


    public static final long TIME_INTERVAL = 1000L;
    private static long mLastClickTime = 0;
    private AddDishCallBack getDishDetailCallBack = new AddDishCallBack() {
        @Override
        public void onClick(Dish dish,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                dishDialog = new DishDialog(dish);
                dishDialog.show(getFragmentManager(),"");

                mLastClickTime = nowTime;
            }

        }
    };
//    private View.OnClickListener listener =new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            dishDialog.dismiss();
//        }
//    };

    private void observeViewModelHotDishList(final DetailViewModel viewModel) {
        viewModel.getHotDishList().observe(this, new Observer<List<Dish>>() {
            @Override
            public void onChanged(List<Dish> dishList) {
                initFlowLayout(dishList);
            }
        });
    }

    private void observeViewModelDishTypeList(final DetailViewModel viewModel) {
        viewModel.getDishTypeList().observe(this, new Observer<List<DishType>>() {
            @Override
            public void onChanged(List<DishType> dishTypeList) {
                dishTypeListAdapter  = new DishTypeListAdapter(dishTypeList);
                viewModel.loadDishList(viewModel.getDishTypeList().getValue().get(0).getId());
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                listView = fragmentDishBinding.typeListview;
                listView.setAdapter(dishTypeListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        dishTypeListAdapter.changeSelected(i);
                        viewModel.loadDishList(viewModel.getDishTypeList().getValue().get(i).getId());


                    }
                });

            }
        });
    }
    private void observeViewModelDishList(final DetailViewModel viewModel) {
        viewModel.getDishList().observe(this, new Observer<List<Dish>>() {
            @Override
            public void onChanged(List<Dish> dishList) {
                dishRecyclerAdapter.setData(dishList);
            }
        });
    }



    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDestroy();
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialogInterface);
        }
    }
}
