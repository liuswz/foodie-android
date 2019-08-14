package com.foodie.user.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.util.BaseUtil;
import com.foodie.base.util.OssManager;

import com.foodie.user.databinding.ActivityPersonBinding;
import com.foodie.user.R;
import com.foodie.user.ui.viewmodel.UserViewModel;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class PersonActivity extends AppCompatActivity {

    private ActivityPersonBinding activityPersonBinding;
    private String username;
    private Integer userId;
    private String photoUrl;
    private UserViewModel viewModel;
    private Integer REQUEST_CODE_ChangeUserName = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        activityPersonBinding = DataBindingUtil.setContentView(this, R.layout.activity_person);
        SharedPreferences sharedPreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
      //  userId = sharedPreferences.getInt("userId", 0);
        photoUrl = sharedPreferences.getString("userPhotoUrl", null);
      //  username = "Owen";
        userId = 1;
        photoUrl = Constant.OPERATER_SHOP_PHOTOURL;
        activityPersonBinding.setPhotoUrl(photoUrl);
        activityPersonBinding.setUsername(username);
        activityPersonBinding.changePhoto.setOnClickListener(changePhoto);
        activityPersonBinding.changeUsername.setOnClickListener(changeUsername);
        activityPersonBinding.backPage.setOnClickListener(backPage);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.setUserId(userId);
        observeViewModelPhotoUrlLiveData(viewModel);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                String photo_name = BaseUtil.getPhotoName()+".jpg";
                activityPersonBinding.setPhotoUrl(picturePath);
                File file=new File(picturePath);
                File compressedImageFile = null;
                try {
                     compressedImageFile = new Compressor(this).compressToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OssManager.getInstance().init(this)
                        .uploadFile("userPhoto/"+photo_name,compressedImageFile.getAbsolutePath());
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userPhotoUrl",photoUrl);
                editor.putString("userPhotoUrlInDisk",compressedImageFile.getAbsolutePath());
                editor.commit();
                photoUrl = Constant.UserPhotoUrl +photo_name;
                viewModel.updatePhotoUrl(photoUrl);

            }
        }else if(requestCode==REQUEST_CODE_ChangeUserName){
            Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
            username = data.getStringExtra("username");
            activityPersonBinding.setUsername(username);
        }
    }
    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
           finish();
        }
    };
    private OnClickEvent changePhoto = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            PictureSelector
                    .create(PersonActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                    .selectPicture(true, 200, 200, 1, 1);
        }
    };
    private OnClickEvent changeUsername = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(PersonActivity.this, ChangeUsernameActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ChangeUserName);
        }
    };
    private void observeViewModelPhotoUrlLiveData(final UserViewModel viewModel) {

        viewModel.getPhotoUrlLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String photoUrl) {

//                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("userPhotoUrl",photoUrl);
//                editor.commit();
                activityPersonBinding.setPhotoUrl(photoUrl);
            }
        });
    }
}
