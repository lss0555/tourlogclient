package com.example.tourlog.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

//修改密码
public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText mEtPasswordOld;
    private EditText mEtPasswordNew;
    private EditText mEtPasswordNew1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        mEtPasswordOld = findViewById(R.id.et_old_password);
        mEtPasswordNew = findViewById(R.id.et_newpassword);
        mEtPasswordNew1 = findViewById(R.id.et_newpassword1);
    }

    //退出
    public void finish(View view) {
        finish();
    }

    //确认修改
    public void updatepass(View view) {
        if (mEtPasswordOld.getText().toString().trim().equals("")
                || mEtPasswordNew.getText().toString().trim().equals("") || mEtPasswordNew1.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入完整", Toast.LENGTH_SHORT).show();
        } else if (!mEtPasswordNew.getText().toString().trim().equals(mEtPasswordNew1.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
        } else if (!MD5Util.MD5(mEtPasswordOld.getText().toString().trim()).equals(CaCheUtils.GetPassword(getApplicationContext()))){
            Toast.makeText(getApplicationContext(), "旧密码有误", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User();
            user.setId(Integer.parseInt(CaCheUtils.GetId(getApplicationContext())));
            user.setPassword(MD5Util.MD5(mEtPasswordNew.getText().toString().trim()));
            HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE + "updatepassword", JSON.toJSONString(user), new DialogCallBack<TResult<User>>(UpdatePasswordActivity.this) {
                @Override
                public void onSuccess(Response<TResult<User>> response) {
                    CaCheUtils.savePassword(getApplicationContext(),mEtPasswordNew.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
