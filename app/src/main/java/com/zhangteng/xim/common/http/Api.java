package com.zhangteng.xim.common.http;

import com.zhangteng.xim.callback.ResponseRetrofitCallBack;
import com.zhangteng.xim.common.config.NetConfig;
import com.zhangteng.xim.common.params.LoginParams;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Lanxumit on 2017/11/29.
 */

public class Api {

    private static Api instance;

    private Api() {
    }

    public static Api getInstance() {
        if (instance == null) {
            synchronized (Api.class) {
                instance = new Api();
            }
        }
        return instance;
    }

    /**
     * 登录
     *
     * @param username 帐号
     * @param password 密码
     * @param callBack
     */
    public void loginApi(String username, String password, ResponseRetrofitCallBack callBack) {
        LoginParams params = new LoginParams();
        params.setUserId(username);
        params.setUserPwd(password);
        HttpRequestManager.getInstance().doGet(NetConfig.NetMethodConfig.LOGIN, params, callBack);
    }
}
