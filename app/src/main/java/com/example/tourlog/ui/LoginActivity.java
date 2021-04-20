package com.example.tourlog.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tourlog.R;
import com.example.tourlog.bean.TResult;
import com.example.tourlog.bean.User;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.okhttp.DialogCallBack;
import com.example.tourlog.okhttp.HttpUtils;
import com.example.tourlog.utils.CaCheUtils;
import com.example.tourlog.utils.Code;
import com.example.tourlog.utils.MD5Util;
import com.example.tourlog.utils.ToastUtils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
/**
 * 登录
 */
public class LoginActivity extends Activity {
    private EditText mEtUser;
    private EditText mEtPassword;
    private TextView mTvRegist;
    private RelativeLayout mRtlComfir;
    private Button mBtnLogin;
    private ImageView mImgCode;
    private EditText mEtCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
        initdata();
        initevent();
    }

    private void initdata() {
        mImgCode.setImageBitmap(Code.getInstance().createBitmap());
        if(CaCheUtils.IsLogin(getApplicationContext())){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void initevent() {
        mImgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgCode.setImageBitmap(Code.getInstance().createBitmap());
            }
        });

        mTvRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistActivity.class));

            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtUser.getText().toString().trim().equals("")||mEtCode.getText().toString().trim().equals("")||mEtPassword.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"请填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    //执行登录操作
                    login();
                }
            }
        });
    }

    //登录
    public void login(){
        if(!Code.getInstance().getCode().toLowerCase().equals(mEtCode.getText().toString().trim().toLowerCase())){
            ToastUtils.showToast("验证码错误");
            mImgCode.setImageBitmap(Code.getInstance().createBitmap());
            return;
        }
        final User user = new User();
        user.setUsername(mEtUser.getText().toString().trim());
        user.setPassword(MD5Util.MD5(mEtPassword.getText().toString().trim()));
        HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE+"login", JSON.toJSONString(user), new DialogCallBack<TResult<User>>(LoginActivity.this) {
            @Override
            public void onSuccess(Response<TResult<User>> response) {
                User userDate = response.body().getModel();
                if(userDate != null){
                    Toast.makeText(getApplicationContext(),"登录成功", Toast.LENGTH_SHORT).show();
                    CaCheUtils.saveId(getApplicationContext(),userDate.getId()+"");
                    CaCheUtils.savePassword(getApplicationContext(),userDate.getPassword()+"");
                    CaCheUtils.saveUser(getApplicationContext(),userDate.getNickname()+"");
                    CaCheUtils.saveIsLogin(getApplicationContext(),true);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();;
                }
            }
        });
    }
    private void initview() {
        mImgCode = (ImageView) findViewById(R.id.img_code);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mEtUser = (EditText) findViewById(R.id.et_user);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvRegist = (TextView) findViewById(R.id.tv_regist);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }
}
