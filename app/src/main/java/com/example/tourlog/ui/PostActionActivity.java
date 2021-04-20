package com.example.tourlog.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 发布
 **/
public class PostActionActivity extends BaseAcitivity {
    private static final String TAG = "PostActionActivity";

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    private ArrayList<String> imagePaths = new ArrayList<>();
    private GdPicAdapter gdPicAdapter;
    private StationaryGridview mGdPic;
    private EditText mEtAddress;
    private EditText mEtDetail;
    private RelativeLayout mRtlPost;
    private Actions mActions;
    private TextView mTvPostStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_action);
        initview();
        initUploadPic();
        initdata();
        initevent();
    }

    private void initview() {
        mGdPic = (StationaryGridview) findViewById(R.id.gd_pic);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mEtDetail = (EditText) findViewById(R.id.et_detail);
        mRtlPost = findViewById(R.id.rtl_comfirm);
        mTvPostStatus = findViewById(R.id.tv_post_status);
    }

    /**
     * 初始化上传图片
     */
    private void initUploadPic() {
        gdPicAdapter = new GdPicAdapter(this, imagePaths);
        mGdPic.setAdapter(gdPicAdapter);
        gdPicAdapter.setOnSelectListner(new GdPicAdapter.OnClickSelectListner() {
            @Override
            public void onshowImgPop() {
                int count = 9 - imagePaths.size();
                PhotoPickerIntent intent = new PhotoPickerIntent(PostActionActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(count); // 最多选择照片数量
//                intent.setSelectedPaths(imagePaths);
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
            }
        });
    }

    private void initdata() {
        mActions = (Actions) getIntent().getSerializableExtra("data");
        if(mActions!=null){
            mTvPostStatus.setText("确认修改");
            mEtAddress.setText(mActions.getAddress());
            mEtDetail.setText(mActions.getContent());
            String imglist = mActions.getImglist();
            ArrayList<String> imgList=new ArrayList<>();
            if(imglist!=null&&!imglist.equals("")){
                String[] split = imglist.split(",");
                if(split.length>0){
                    for (String str:split){
                        imgList.add(str);
                    }
                }
            }
            imagePaths.addAll(imgList);
            gdPicAdapter.notifyDataSetChanged();
        }
    }

    private void initevent() {
        //删除
        gdPicAdapter.setOnDelImgLitner(new GdPicAdapter.onDelImgLitner() {
            @Override
            public void del(int pos) {
                HttpUtils.getInstance().get(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE + "delImg?name=" + imagePaths.get(pos), null, new DialogCallBack<TResult<User>>(PostActionActivity.this) {
                    @Override
                    public void onSuccess(Response<TResult<User>> response) {
                        imagePaths.remove(pos);
                        gdPicAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


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
                    actions.setType(1);
                    actions.setContent(mEtDetail.getText().toString().trim());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    actions.setCreatTime(df.format(new Date()));
                    String imgs = "";
                    if (imagePaths.size() > 0) {
                        for (int i = 0; i < imagePaths.size(); i++) {
                            if (i == 0) {
                                imgs = imagePaths.get(i);
                            } else {
                                imgs = imgs + "," + imagePaths.get(i);
                            }
                        }
                    }
                    actions.setImglist(imgs);
                    //TODO
                    actions.setXc_id(1);
                    HttpUtils.getInstance().post(getApplicationContext(), CacheMode.NO_CACHE, HTTPURL.BASE + "postAction", JSON.toJSONString(actions), new DialogCallBack<TResult<User>>(PostActionActivity.this) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "图片选择：" + list);
//                    loadAdpater(list);
                    if (list.size() > 0) {
                        uploadFile(list);
                    }
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
//                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    //上传文件
    public void uploadFile(List<String> paths) {
        ArrayList<File> files = new ArrayList<>();
        if (paths.size() > 0) {
            for (String path : paths) {
                files.add(new File(path));
            }
        }
        HttpParams param = new HttpParams();
        param.put("userid", CaCheUtils.GetId(getApplicationContext()));
        OkGo.<String>post(HTTPURL.BASE + "uploadMultiImgList")
                .tag(this)
                .addFileParams("files", files)
                .params(param)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        TResult<String> tResult = JSON.parseObject(body, TResult.class);
                        List<String> stringList = tResult.getData();
                        imagePaths.addAll(stringList);
                        gdPicAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showToast("上传失败");
                    }
                });
    }
}

