package com.example.tourlog.okhttp;

import com.example.tourlog.bean.TResult;
import com.example.tourlog.utils.ToastUtils;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * * 抽象Json CallBack
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {
    public JsonCallback() {
        super();
    }
    @Override
    public T convertResponse(Response response) throws Throwable {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");

        JsonReader jsonReader = new JsonReader(response.body().charStream());
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType == TResult.class) {
            TResult<T> result = Convert.fromJson(jsonReader, type);
            if(result.getErrCode()==null){
                throw new IllegalStateException("基类错误无法解析");
            }
            if (result.getErrCode()==0) {
                response.close();
                return (T) result;
            } else {
                response.close();
                if(result!=null&&result.getErrMsg()!=null){
                    throw new IllegalStateException(""+result.getErrMsg());
                }else {
                    throw new IllegalStateException("接口错误");
                }

            }
        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        String error = ApiException.handleException(response.getException()).getMessage();
        if(error!=null&& !error.equals("")){
            ToastUtils.showToast(error);
        }
    }
}
