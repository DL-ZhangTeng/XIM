package com.zhangteng.xim.mvp.view;

import com.zhangteng.xim.mvp.base.BaseView;


public interface ChangePasswordView extends BaseView {
    void setUserName(String userName);

    String getUserName();

    void setPassword(String password);

    String getPassword();

    void setCode(String code);

    String getCode();

    void setResultFinish();

    void showToast(String toast);
}
