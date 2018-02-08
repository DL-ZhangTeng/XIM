package com.zhangteng.xim.common.params;

/**
 * Created by Lanxumit on 2017/11/29.
 */

public class BaseParams {

    private String Module;
    private String Stamp;
    private String Token;

    public BaseParams() {
    }

    public BaseParams(String module, String stamp, String token) {
        Module = module;
        Stamp = stamp;
        Token = token;
    }

    public String getModule() {
        return Module;
    }

    public void setModule(String module) {
        Module = module;
    }

    public String getStamp() {
        return Stamp;
    }

    public void setStamp(String stamp) {
        Stamp = stamp;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
