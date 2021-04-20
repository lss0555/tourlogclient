package com.example.tourlog.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tourlog.R;
import com.example.tourlog.bean.TResult;
import com.example.tourlog.bean.User;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.okhttp.DialogCallBack;
import com.example.tourlog.okhttp.HttpUtils;
import com.example.tourlog.utils.MD5Util;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;

/**
 * 用户注册
 */
public class RegistActivity extends Activity {
    private EditText mEtPassword;
    private EditText mEtNickName;
    private EditText mEtUser;
    private EditText mEtEmail;
    private RelativeLayout mRtlComfirm;
    private EditText mEtPassword1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initview();
        initevent();
    }
    private void initview() {
        mEtNickName = (EditText) findViewById(R.id.et_nickname);
        mEtUser = (EditText) findViewById(R.id.et_user);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword1 = (EditText) findViewById(R.id.et_password1);
        mRtlComfirm = (RelativeLayout) findViewById(R.id.rtl_comfirm);
    }
    private void initevent() {
        findViewById(R.id.rtl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRtlComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtUser.getText().toString().trim().equals("") ||
                        mEtNickName.getText().toString().trim().equals("") ||
                        mEtPassword.getText().toString().trim().equals("") ||
                        mEtEmail.getText().toString().trim().equals("") ||
                        mEtPassword1.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),"请填写完整", Toast.LENGTH_SHORT).show();
                }else if(!mEtPassword.getText().toString().trim().equals(mEtPassword1.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),"两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else{
                    regist();
                }
            }
        });

    }
    //注册操作
    public void regist(){
        User user = new User();
        user.setPassword(MD5Util.MD5(mEtPassword.getText().toString().trim()));
        user.setNickname(mEtNickName.getText().toString().trim());
        user.setUsername(mEtUser.getText().toString().trim());
        user.setEmail(mEtEmail.getText().toString().trim());
        HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE+"addUser", JSON.toJSONString(user), new DialogCallBack<TResult<User>>(RegistActivity.this) {
            @Override
            public void onSuccess(Response<TResult<User>> response) {
                Toast.makeText(getApplicationContext(),"注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void finish(View view) {
        finish();
    }
}

