package com.zhangteng.xim.mvp.presenter;

import com.zhangteng.xim.mvp.base.BasePresenter;
import com.zhangteng.xim.mvp.model.ChangePasswordModel;
import com.zhangteng.xim.mvp.view.ChangePasswordView;


public class ChangePasswordPresenter extends BasePresenter implements ChangePasswordView {
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
}
