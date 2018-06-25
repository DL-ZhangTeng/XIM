package com.zhangteng.xim.mvp.presenter;

import android.content.Context;

import com.zhangteng.xim.mvp.base.BasePresenter;
import com.zhangteng.xim.mvp.model.ChangePasswordModel;
import com.zhangteng.xim.mvp.view.ChangePasswordView;


public class ChangePasswordPresenter extends BasePresenter {
    private ChangePasswordView view;
    private ChangePasswordModel model;

    public ChangePasswordPresenter(ChangePasswordView view, ChangePasswordModel model) {
        this.view = view;
        this.model = model;
    }

    public ChangePasswordPresenter(ChangePasswordView view) {
        this.view = view;
        this.model = new ChangePasswordModel();
    }

    public void clearUsername() {
        view.setUserName("");
    }

    public void clearPassword() {
        view.setPassword("");
    }

    public void clearCode() {
        view.setCode("");
    }

    public void changePwd(Context context) {
        model.changePwd(context, this);
    }

    public String getUserName() {
        return view.getUserName();
    }


    public String getPassword() {
        return view.getPassword();
    }


    public String getCode() {
        return view.getCode();
    }

    public void setResultFinish() {
        view.setResultFinish();
    }

    public void showToast(String toast) {
        view.showToast(toast);
    }
}
