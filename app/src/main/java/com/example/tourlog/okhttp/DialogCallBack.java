package com.example.tourlog.okhttp;

import android.content.Context;

import com.lzy.okgo.request.base.Request;

/**
 * 抽象加载弹出框callback
 */
public abstract class DialogCallBack<T> extends JsonCallback<T> {
    private LoadingDialog dialog ;

    public DialogCallBack(Context context) {
        super();
        if (null == dialog) {
            dialog = new LoadingDialog(context);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (!dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onFinish() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }
}
