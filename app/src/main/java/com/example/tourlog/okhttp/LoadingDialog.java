package com.example.tourlog.okhttp;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourlog.R;

/**
 * 加载弹出框自定义
 */
public class LoadingDialog extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';
    private TextView mTvTitle;
    private TextView mTvPoint;
    private RotateAnimation mAnim;
    private Handler handler = new Handler() {
        private int num = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num++;
                for (int i = 0; i < num; i++) {
                    builder.append(SUFFIX);
                }
                mTvPoint.setText(builder.toString());
                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            }
        }
    };
    private Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_loading_layout);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        ImageView mImgLoad=(ImageView) findViewById(R.id.img_load);
//        Glide.with(context).load(R.mipmap.load_gif).into(mImgLoad);
        initAnim();
//        getWindow().setWindowAnimations(R.anim.alpha_in);
    }

    private void initAnim() {
        mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
    }

    @Override
    public void show() {//在要用到的地方调用这个方法
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
        super.show();
    }

    @Override
    public void dismiss() {
        mAnim.cancel();
        super.dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            mTvTitle.setText("正在加载");
        } else {
            mTvTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

}