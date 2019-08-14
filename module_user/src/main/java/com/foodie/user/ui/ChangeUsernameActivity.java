package com.foodie.user.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.enums.ResultCode;

import com.foodie.user.R;
import com.foodie.user.databinding.ActivityChangeUsernameBinding;
import com.foodie.user.ui.viewmodel.UserViewModel;

public class ChangeUsernameActivity extends AppCompatActivity {
    private ActivityChangeUsernameBinding activityChangeUsernameBinding;
    private String username;
    private UserViewModel viewModel;
    private Integer userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        activityChangeUsernameBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_username);

        SharedPreferences sharedPreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
      //  userId = sharedPreferences.getInt("userId", 0);

      //  username = "Owen";
        userId = 1;


        activityChangeUsernameBinding.setUsername(username);
        activityChangeUsernameBinding.save.setOnClickListener(save);
        activityChangeUsernameBinding.backPage.setOnClickListener(backPage);
        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.setUserId(userId);
        observeViewModelUsernameLiveData(viewModel);
    }

    private OnClickEvent save = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            String newUsername = activityChangeUsernameBinding.usernameEditText.getText().toString().trim();
            if(TextUtils.isEmpty(newUsername)){
                showDialog("用户名不能为空");
            }else{
                viewModel.updateUsername(newUsername);
            }
        }
    };
    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(ChangeUsernameActivity.this, PersonActivity.class);
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };
    private void observeViewModelUsernameLiveData(final UserViewModel viewModel) {

        viewModel.getUsernameLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String username) {

                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",username);
                editor.commit();
                Intent intent = new Intent(ChangeUsernameActivity.this, PersonActivity.class);
                intent.putExtra("username", username);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void showDialog(String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUsernameActivity.this);
        builder.setTitle(value).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
}
