package com.zhangteng.xim.mvp.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.LoginParams;
import com.zhangteng.xim.mvp.base.BaseModel;
import com.zhangteng.xim.mvp.presenter.ChangePasswordPresenter;

import cn.bmob.v3.exception.BmobException;

public class ChangePasswordModel extends BaseModel {

    public void changePwd(final Context context, final ChangePasswordPresenter changePasswordPresenter) {
        if (TextUtils.isEmpty(changePasswordPresenter.getUserName()) || TextUtils.isEmpty(changePasswordPresenter.getPassword())) {
            changePasswordPresenter.showToast("username or password is null");
        }
        LoginParams loginParams = new LoginParams();
        loginParams.setName(changePasswordPresenter.getUserName());
        loginParams.setPassword(changePasswordPresenter.getPassword());
        UserApi.getInstance().login(loginParams, new BmobCallBack<User>(context, false) {
            @Override
            public void onSuccess(@Nullable User bmobObject) {
                UserApi.getInstance().resetPassword(changePasswordPresenter.getCode(), changePasswordPresenter.getPassword(), new BmobCallBack(context, true) {
                    @Override
                    public void onSuccess(@Nullable Object bmobObject) {
                        changePasswordPresenter.setResultFinish();
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        changePasswordPresenter.showToast(bmobException.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                changePasswordPresenter.showToast(bmobException.getMessage());
            }
        });
    }
}
