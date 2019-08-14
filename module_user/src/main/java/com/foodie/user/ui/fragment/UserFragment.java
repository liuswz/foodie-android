package com.foodie.user.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.base.router.RouterFragmentPath;
import com.foodie.user.R;
import com.foodie.user.databinding.FragmentUserBinding;
import com.foodie.user.ui.ChangeUsernameActivity;
import com.foodie.user.ui.CollectionAcitivity;
import com.foodie.user.ui.MyVoucherActivity;
import com.foodie.user.ui.PersonActivity;
import com.foodie.user.ui.RuZhuActivity;
import com.foodie.user.ui.VoucherActivity;
import com.foodie.user.ui.viewmodel.UserViewModel;


/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.User.PAGER_USER)
public class UserFragment extends Fragment {
    private FragmentUserBinding fragmentUserBinding;
    private UserViewModel viewModel;
    private String username;
    private Integer userId;
    private String photoUrl;
    private Integer loginFlag;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentUserBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        return fragmentUserBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // username = "Owen";
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        fragmentUserBinding.checkDetail.setOnClickListener(checkDetail);
        fragmentUserBinding.getVoucher.setOnClickListener(getVoucher);
        fragmentUserBinding.getMyVoucher.setOnClickListener(getMyVoucher);
        fragmentUserBinding.shoucang.setOnClickListener(shoucang);
        fragmentUserBinding.registerLequ.setOnClickListener(registerLequ);
        fragmentUserBinding.zhuxiao.setOnClickListener(zhuxiao);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username","Owen");
//        editor.putString("userPhotoUrl",Constant.OPERATER_SHOP_PHOTOURL);
//        editor.commit();
        username = sharedPreferences.getString("username", null);
        //  userId = sharedPreferences.getInt("userId", 0);
        String userPhotoUrlInDisk = sharedPreferences.getString("userPhotoUrlInDisk", null);
        if(userPhotoUrlInDisk!=null){
            photoUrl = userPhotoUrlInDisk;
        } else{
            photoUrl = sharedPreferences.getString("userPhotoUrl", null);
        }
       // username="Owen";
        userId = 1;
       // photoUrl = Constant.OPERATER_SHOP_PHOTOURL;
        if(userId==null||username==null||photoUrl==null){
            fragmentUserBinding.setUsername("请登入");
            loginFlag=0;
        }else{
            fragmentUserBinding.setPhotoUrl(photoUrl);
            fragmentUserBinding.setUsername(username);
            loginFlag=1;
        }        //初始化定位

    }

    private OnClickEvent zhuxiao = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            if(loginFlag ==0){
                showDialog("您还没有登入呢");
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确定注销吗")
                        // .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("userPhotoUrl");
                                editor.remove("userPhotoUrlInDisk");
                                editor.remove("username");
                                editor.remove("userId");
                                editor.remove("userPhoneNum");
                                editor.commit();
                                fragmentUserBinding.setUsername("请登入");
                                fragmentUserBinding.userPhoto.setBackgroundResource(R.drawable.icon_person);
                                fragmentUserBinding.setPhotoUrl(null);
                                loginFlag=0;


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();

            }
        }
    };
    private OnClickEvent shoucang = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getActivity(), CollectionAcitivity.class);
            startActivity(intent);
        }
    };
    private OnClickEvent registerLequ = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getActivity(), RuZhuActivity.class);
            startActivity(intent);
        }
    };
    private OnClickEvent getVoucher = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getActivity(), VoucherActivity.class);
            startActivity(intent);
        }
    };
    private OnClickEvent getMyVoucher = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getActivity(), MyVoucherActivity.class);
            startActivity(intent);
        }
    };

    private OnClickEvent checkDetail = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            if(loginFlag==0){
                ARouter.getInstance().build(RouterActivityPath.Login.ACTIVITYLOGIN)
                        .navigation();
            }else{
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }

        }
    };
    private void showDialog(String value){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(value).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
}
