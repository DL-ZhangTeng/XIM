package com.zhangteng.xim.common.params;

/**
 * 1.登录
 * Created by Lanxumit on 2017/12/6.
 */

public class LoginParams {

    private String userId;
    private String userPwd;

    public LoginParams() {
    }

    public LoginParams(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
