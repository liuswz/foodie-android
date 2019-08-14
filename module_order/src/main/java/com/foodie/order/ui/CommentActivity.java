package com.foodie.order.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.enums.GoodType;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.util.BaseUtil;
import com.foodie.order.R;
import com.foodie.order.databinding.ActivityCommentBinding;
import com.foodie.base.entity.ProductComment;
import com.foodie.base.entity.ShopComment;
import com.foodie.order.ui.viewmodel.CommentViewModel;
import com.foodie.base.util.OssManager;
import com.wildma.pictureselector.PictureSelector;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding activityCommentBinding;
    private CommentViewModel viewModel;
    private Integer goodId;
    private Integer orderId;
    private Integer goodType;
    private Double mark = 6D;
    private String photoUrl;
    private Integer userId;
    private String userPhotoUrl;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        activityCommentBinding =  DataBindingUtil.setContentView(this, R.layout.activity_comment);
        viewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        Intent intent  =getIntent();
        orderId = Integer.parseInt(intent.getStringExtra("orderId"));
        goodId = Integer.parseInt(intent.getStringExtra("goodId"));
        goodType = Integer.parseInt(intent.getStringExtra("goodType"));
        activityCommentBinding.confirm.setOnClickListener(confirm);
        activityCommentBinding.addPhoto.setOnClickListener(addPhoto);
        activityCommentBinding.ratingbar.setOnRatingBarChangeListener(chooseStar);
        userId = 1;
        userPhotoUrl = "https://lanke-foodie.oss-cn-beijing.aliyuncs.com/shop/15625802699228713";
        username = "Owne";
        observeViewModelPayResponse(viewModel);

    }
    private OnClickEvent addPhoto = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            PictureSelector
                    .create(CommentActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                    .selectPicture(false, 200, 200, 1, 1);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                String photo_name = BaseUtil.getPhotoName();
                activityCommentBinding.setLookPhoto(picturePath);
                OssManager.getInstance().init(this)
                        .uploadFile("comment/"+photo_name,picturePath);
                photoUrl = Constant.CommentPhotoUrl +photo_name;

            }
        }
    }

    private OnClickEvent confirm = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            String content = activityCommentBinding.content.getText().toString().trim();
            if(mark==null){
                showDialog("评分不能为空");
            }else if(TextUtils.isEmpty(content)){
                showDialog("评分内容不能为空");
            }else{
                if(goodType== GoodType.Dish.getIndex()){
                    ShopComment shopComment = new ShopComment();
                    shopComment.setContent(content);
                    shopComment.setMark(mark);
                    shopComment.setOrderId(orderId);
                    shopComment.setShopId(goodId);
                    shopComment.setUserId(userId);
                    shopComment.setUsername(username);
                    shopComment.setUserPhotoUrl(userPhotoUrl);
                    shopComment.setPhotoUrl(photoUrl);
                    viewModel.addShopComment(shopComment);
                }else{
                    ProductComment productComment = new ProductComment();
                    productComment.setContent(content);
                    productComment.setMark(mark);
                    productComment.setOrderId(orderId);
                    productComment.setProductId(goodId);
                    productComment.setUserId(userId);
                    productComment.setUsername(username);
                    productComment.setUserPhotoUrl(userPhotoUrl);
                    productComment.setPhotoUrl(photoUrl);
                    viewModel.addProductComment(productComment);
                }




            }


        }
    };

    private void observeViewModelPayResponse(final CommentViewModel viewModel) {

        viewModel.getaddResponse().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer flag) {
                if(flag== ResultCode.SUCCESS.getIndex()){
                    Toast.makeText(CommentActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private RatingBar.OnRatingBarChangeListener chooseStar = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
           // Toast.makeText(CommentActivity.this,"rating"+String.valueOf(v),Toast.LENGTH_SHORT).show();
            mark = Double.parseDouble(String.valueOf(v*2));
        }
    };

    private void showDialog(String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
        builder.setTitle(value).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
}
