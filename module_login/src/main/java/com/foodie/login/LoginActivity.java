package com.foodie.login;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.User;
import com.foodie.login.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel ;
    private ActivityLoginBinding activityLoginBinding;
    private Timer tm;
    private TimerTask tt;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country = "86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private String phone;
    private static final int CODE_REPEAT = 1; //重新发送

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        SMSSDK.registerEventHandler(eh);
        activityLoginBinding.btLogin.setOnClickListener(gotologin);
        activityLoginBinding.btGetYanzheng.setOnClickListener(getYanzheng);
        activityLoginBinding.wechatLogin.setOnClickListener(wechatLogin);
        activityLoginBinding.qqLogin.setOnClickListener(qqLogin);

        observeViewModelUserData(viewModel);
       // sendCode(this);
    }

    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                activityLoginBinding.btGetYanzheng.setEnabled(true);
                activityLoginBinding.btLogin.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                activityLoginBinding.btGetYanzheng.setText("重新发送验证码");
            } else {
                activityLoginBinding.btGetYanzheng.setText(TIME+"s" );
            }
        }
    };
    //回调
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    viewModel.loginSuccess(phone);


                    toast("验证成功");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    toast("获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            } else {//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                ((Throwable) data).printStackTrace();
                String str = data.toString();
                toast(str);
            }
        }
    };
    private void toast(final String str) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public OnClickEvent wechatLogin = new OnClickEvent() {
        @Override
        public void singleClick(View videw) {


        }
    };
    public OnClickEvent qqLogin = new OnClickEvent() {
        @Override
        public void singleClick(View videw) {


        }
    };
    public OnClickEvent gotologin = new OnClickEvent() {
        @Override
        public void singleClick(View videw) {
            String code = activityLoginBinding.etYanzheng.getText().toString().replaceAll("/s", "");
            if (!TextUtils.isEmpty(code)) {//判断验证码是否为空
                //验证
                SMSSDK.submitVerificationCode(country, phone, code);
            } else {//如果用户输入的内容为空，提醒用户
                toast("请输入验证码后再提交");
            }

        }
    };
    public OnClickEvent getYanzheng = new OnClickEvent() {
        @Override
        public void singleClick(View videw) {
            phone = activityLoginBinding.etPhone.getText().toString().trim().replaceAll("/s", "");
            if (!TextUtils.isEmpty(phone)) {
                //定义需要匹配的正则表达式的规则
                String REGEX_MOBILE_SIMPLE = "[1][35678]\\d{9}";
                //把正则表达式的规则编译成模板
                Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                //把需要匹配的字符给模板匹配，获得匹配器
                Matcher matcher = pattern.matcher(phone);
                // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                if (matcher.find()) {//匹配手机号是否存在
                    SMSSDK.getVerificationCode(country, phone);
                    //做倒计时操作
                    Toast.makeText(LoginActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                    activityLoginBinding.btGetYanzheng.setEnabled(false);
                    activityLoginBinding.btLogin.setEnabled(true);
                    tm = new Timer();
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            hd.sendEmptyMessage(TIME--);
                        }
                    };
                    tm.schedule(tt, 0, 1000);
                } else {
                    toast("手机号格式错误");
                }
            } else {
                toast("请先输入手机号");
            }

        }
    };
    private void observeViewModelUserData(final LoginViewModel viewModel) {

        viewModel.getUserdata().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",user.getUsername() );
                editor.putInt("userId", user.getId());
                editor.putString("userPhotoUrl",user.getPhotoUrl() );
                editor.putString("userPhoneNum",user.getPhoneNum() );
                editor.commit();
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    startActivity(intent);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
//    public void sendCode(Context context) {
//        RegisterPage page = new RegisterPage();
//
//        //如果使用我们的ui，没有申请模板编号的情况下需传null
//        page.setTempCode(null);
//        page.setContentViewLayoutResName("登入");
//        page.setRegisterCallback(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    // 处理成功的结果
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
//                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
//                    // TODO 利用国家代码和手机号码进行后续的操作
//                } else{
//                    // TODO 处理错误的结果
//                }
//            }
//        });
//        page.show(context);
//    }
}
