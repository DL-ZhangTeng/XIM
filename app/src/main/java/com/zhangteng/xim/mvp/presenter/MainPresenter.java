package com.zhangteng.xim.mvp.presenter;

import android.content.Context;

import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.mvp.base.BasePresenter;
import com.zhangteng.xim.mvp.view.MainView;
import com.zhangteng.xim.mvp.model.MainModel;


public class MainPresenter extends BasePresenter {
    private MainView view;
    private MainModel model;

    public MainPresenter(MainView view, MainModel model) {
        this.view = view;
        this.model = model;
    }

    public MainPresenter(MainView view) {
        this.view = view;
        this.model = new MainModel();
    }

    public void connectService(Context context) {
        model.connectService(context, this);
    }

    public void showToast(String toast) {
        view.showToast(toast);
    }

    public User getUser() {
        return model.getUser();
    }

    public void queryHeaderViewBg(Context context) {
        model.queryHeaderViewBg(context, this);
    }

    public void setHeaderViewBg(Photo photo) {
        view.setHeaderViewBg(photo);
    }

    public void setUsername() {
        view.setUsername(model.getUser().getUsername());
    }

    public void setAvatar() {
        view.setAvatar(model.getUser().getIcoPath());
    }

    public void setTitleBarLeftIcon() {
        view.setTitleBarLeftIcon(model.getUser().getIcoPath());
    }
}
