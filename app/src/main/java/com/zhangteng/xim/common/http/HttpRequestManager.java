package com.zhangteng.xim.common.http;

import android.util.Log;

import com.google.gson.Gson;
import com.zhangteng.xim.callback.ResponseRetrofitCallBack;
import com.zhangteng.xim.common.tools.Encryption;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Lanxumit on 2017/12/21.
 */

public class HttpRequestManager {

    private static HttpRequestManager instance;

    public static HttpRequestManager getInstance() {
        if (instance == null) {
            instance = new HttpRequestManager();
        }
        return instance;
    }

    public static RequestBody createRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private static String reqParams(Object src) {
        String json = "";
        if (src != null && src instanceof String) {
            json = (String) src;
        } else {
            if (src == null) {
            } else {
                json = new Gson().toJson(src);
            }
        }
        return json;
    }

    public void doGet(String key, Object obj, ResponseRetrofitCallBack callBack) {
        requestMethod(key, obj, callBack);
    }

    public void uploadFile(File file, ResponseRetrofitCallBack callback) {
        callback.onStart();
//        RequestBody keyBody=createRequestBody(key);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        Log.e("eeeeeeee", prefix);
        String realFileName = java.util.UUID.randomUUID().toString() + prefix;
        MultipartBody.Part body = MultipartBody.Part.createFormData("img_file", realFileName, requestFile);
        Call<String> call = HttpUtils.getInstance().uploadFile(body);
        call.enqueue(callback);
    }

    private void requestMethod(String key, Object obj, ResponseRetrofitCallBack callBack) {
        callBack.onStart();
        String json = reqParams(obj);
        //		  加密
        int size = json.length() % 16;
        String replenish = "";
        if (size != 0) {
            for (int i = 0; i < 16 - size; i++) {
                replenish += " ";
            }
        }
        String valuejson = Encryption.encrypt(json + replenish);

        Call<String> call = HttpUtils.getInstance().doGet(key, valuejson);
        call.enqueue(callBack);
    }
}
