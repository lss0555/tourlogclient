package com.example.tourlog.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.example.tourlog.R;
import com.example.tourlog.adapter.GdPicAdapter;
import com.example.tourlog.bean.Actions;
import com.example.tourlog.bean.TResult;
import com.example.tourlog.bean.User;
import com.example.tourlog.config.HTTPURL;
import com.example.tourlog.okhttp.DialogCallBack;
import com.example.tourlog.okhttp.HttpUtils;
import com.example.tourlog.photopicker.PhotoPickerActivity;
import com.example.tourlog.photopicker.PhotoPreviewActivity;
import com.example.tourlog.photopicker.SelectModel;
import com.example.tourlog.photopicker.intent.PhotoPickerIntent;
import com.example.tourlog.utils.CaCheUtils;
import com.example.tourlog.utils.ToastUtils;
import com.example.tourlog.view.StationaryGridview;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description 发布vedio
 **/
public class PostActionVedioActivity extends BaseAcitivity {
    private static final String TAG = "PostActionVedioActivity";
    private EditText mEtAddress;
    private EditText mEtDetail;
    private RelativeLayout mRtlPost;
    private Actions mActions;
    private TextView mTvPostStatus;
    private ImageView mImageVedio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_action_vedio);
        initview();
        initdata();
        initevent();
    }

    private void initview() {
        mImageVedio = (ImageView) findViewById(R.id.img_vedio);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mEtDetail = (EditText) findViewById(R.id.et_detail);
        mRtlPost = findViewById(R.id.rtl_comfirm);
        mTvPostStatus = findViewById(R.id.tv_post_status);
    }


    private void initdata() {
        mActions = (Actions) getIntent().getSerializableExtra("data");
        if(mActions!=null){
            if(mActions.getVediopath()!=null&&!mActions.getVediopath().equals("")){
                Bitmap netVideoBitmap = getNetVideoBitmap(HTTPURL.VEDIO + mActions.getVediopath());
                mImageVedio.setImageBitmap(netVideoBitmap);
            }
            if(mActions.getAddress()!=null&&!mActions.getAddress().equals("")){
                mEtAddress.setText(mActions.getAddress());
            }
            if(mActions.getContent()!=null&&!mActions.getContent().equals("")){
                mEtDetail.setText(mActions.getContent());
            }
        }
    }

    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    private void initevent() {
        mRtlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtAddress.getText().toString().trim().equals("")
                        || mEtDetail.getText().toString().trim().equals("")) {
                    ToastUtils.showToast("请输入完整");
                } else {
                    Actions actions = new Actions();
                    actions.setUserid(Integer.parseInt(CaCheUtils.GetId(getApplicationContext())));
                    actions.setId(mActions.getId());
                    actions.setAddress(mEtAddress.getText().toString().trim());
                    actions.setType(2);
                    actions.setVediopath(mActions.getVediopath());
                    actions.setContent(mEtDetail.getText().toString().trim());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    actions.setCreatTime(df.format(new Date()));
                    //TODO
                    actions.setXc_id(1);
                    HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE + "postAction", JSON.toJSONString(actions), new DialogCallBack<TResult<User>>(PostActionVedioActivity.this) {
                        @Override
                        public void onSuccess(Response<TResult<User>> response) {
                            ToastUtils.showToast("发布成功");
                            setResult(1);
                            finish();
                        }
                    });
                }
            }
        });
    }


}

