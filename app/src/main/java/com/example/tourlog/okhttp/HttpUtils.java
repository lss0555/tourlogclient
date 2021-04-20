package com.example.tourlog.okhttp;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpMethod;

import java.util.HashMap;

/**
 * @Description http工具类
 **/
public class HttpUtils{
    public static HttpUtils instance;
    public static HttpUtils getInstance(){
        if(instance==null){
            instance=new HttpUtils();
        }
        return instance;
    }
    public<T> void post(Context context, CacheMode cacheMode , String url, String json, Object tag, JsonCallback<T> callback) {
        OkGo.<T>post(url).cacheMode(cacheMode).upJson(json).tag(tag).execute(callback);
    }
    public<T> void post(Context context, CacheMode cacheMode, String url, String json, JsonCallback<T> callback) {
        OkGo.<T>post(url).cacheMode(cacheMode).upJson(json).tag(context).execute(callback);
    }

    public<T> void get(Context context, CacheMode cacheMode , String url, final HashMap<String, String> map, Object tag, JsonCallback<T> callback) {
        OkGo.<T>get(url).cacheMode(cacheMode).params(map).tag(tag).execute(callback);
    }
    public<T> void get(Context context, CacheMode cacheMode , String url, final HashMap<String, String> map, JsonCallback<T> callback) {
        OkGo.<T>get(url).cacheMode(cacheMode).params(map).tag(context).execute(callback);
    }

    public<T> void put(Context context, CacheMode cacheMode , String url, String json, Object tag, JsonCallback<T> callback) {
        OkGo.<T>put(url).cacheMode(cacheMode).upJson(json).tag(tag).execute(callback);
    }
    public<T> void put(Context context, CacheMode cacheMode , String url, String json, JsonCallback<T> callback) {
        OkGo.<T>put(url).cacheMode(cacheMode).upJson(json).tag(context).execute(callback);
    }
    /**
     * 取消所有请求
     */
    public void cancelAll() {
        OkGo.getInstance().cancelAll();
    }
    /**
     * 通过tag取消请求
     * @param tag 请求的时候设置的tag
     */
    public void cancelTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }
}
