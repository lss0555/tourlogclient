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
import com.example.tourlog.utils.CaCheUtils;
import com.example.tourlog.utils.MD5Util;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;

/**
 * 用户信息修改
 */
public class UpdateUserInfoActivity extends Activity {
    private EditText mEtNickName;
    private EditText mEtEmail;
    private RelativeLayout mRtlComfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        initview();
        initdata();
        initevent();
    }

    private void initdata() {
        User user= (User) getIntent().getSerializableExtra("data");
        if(user!=null){
           mEtEmail.setText(user.getEmail());
           mEtNickName.setText(user.getNickname());
        }
    }

    private void initview() {
        mEtNickName = (EditText) findViewById(R.id.et_nickname);
        mEtEmail = (EditText) findViewById(R.id.et_email);
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
                if (    mEtNickName.getText().toString().trim().equals("") ||
                        mEtEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),"请填写完整", Toast.LENGTH_SHORT).show();
                }else{
                    updateinfo();
                }
            }
        });

    }
    //修改信息操作
    public void updateinfo(){
        User user = new User();
        user.setId(Integer.parseInt(CaCheUtils.GetId(getApplicationContext())));
        user.setNickname(mEtNickName.getText().toString().trim());
        user.setEmail(mEtEmail.getText().toString().trim());
        HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE+"updateUser", JSON.toJSONString(user), new DialogCallBack<TResult<User>>(UpdateUserInfoActivity.this) {
            @Override
            public void onSuccess(Response<TResult<User>> response) {
                Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_SHORT).show();
                CaCheUtils.saveUser(getApplicationContext(),user.getNickname());
                setResult(1);
                finish();
            }
        });
    }

    public void finish(View view) {
        finish();
    }
}

