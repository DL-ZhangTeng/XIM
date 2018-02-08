package com.zhangteng.xim.common.http;

import com.zhangteng.xim.common.config.NetConfig;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Lanxumit on 2017/11/29.
 */

public interface HttpService {

    @GET(NetConfig.PATH)
    Call<String> doGet(@Query("key") String key, @Query("param") String body);

    @Multipart
    @POST("http://000.000.000.000:000/upload?key=uploadFile")
    Call<String> uploadFile(@Part MultipartBody.Part file);

}
