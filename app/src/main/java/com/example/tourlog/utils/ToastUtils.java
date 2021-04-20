package com.example.tourlog.utils;

import android.widget.Toast;

import com.example.tourlog.base.BaseApplication;

/**
 * 全局弹出框
 */

public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(String msg) {
        if(msg==null){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.application, msg, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(msg);
        }
        toast.show();
    }
}
