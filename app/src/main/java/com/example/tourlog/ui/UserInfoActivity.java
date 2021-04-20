package com.example.tourlog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourlog.R;
import com.example.tourlog.bean.TResult;
import com.example.tourlog.bean.User;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.okhttp.DialogCallBack;
import com.example.tourlog.okhttp.HttpUtils;
import com.example.tourlog.utils.CaCheUtils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;

//个人信息
public class UserInfoActivity extends BaseAcitivity {

    private TextView mTvNickname;
    private TextView mTvusername;
    private TextView mTvEmail;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        mTvEmail = findViewById(R.id.tv_email);
        mTvNickname = findViewById(R.id.tv_nickname);
        mTvusername = findViewById(R.id.tv_username);

        initdata();

    }

    private void initdata() {
        HttpUtils.getInstance().get(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE + "getUserInfoByid?id="+ CaCheUtils.GetId(getApplicationContext()), null, new DialogCallBack<TResult<User>>(UserInfoActivity.this) {
            @Override
            public void onSuccess(Response<TResult<User>> response) {
                user = response.body().getModel();
                if(user !=null){
                    mTvusername.setText(""+ user.getUsername());
                    mTvNickname.setText(""+ user.getNickname());
                    mTvEmail.setText(""+ user.getEmail());
                }
            }
        });
    }

    public void finish(View view) {
        finish();
    }

    //修改
    public void updateinfo(View view) {
        startActivityForResult(new Intent(getApplicationContext(),UpdateUserInfoActivity.class).putExtra("data",user),11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            initdata();
        }
    }
}
