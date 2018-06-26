package com.zhangteng.xim.mvp.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.mvp.base.BasePresenter;
import com.zhangteng.xim.mvp.model.MainModel;
import com.zhangteng.xim.mvp.view.MainView;

import java.io.File;


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

    public void updateVersion(Context context, FragmentManager fragmentManager) {
        model.updateVersion(context, fragmentManager);
    }

    public void sendAddFriendMessage(Context context, String result) {
        model.sendAddFriendMessage(context, result, this);
    }

    public void uploadThemePhoto(Context context) {
        Photo photo = new Photo();
        photo.setUser(getUser());
        photo.setMark("theme");
        photo.setName(getCameraTempFile().getName());
        model.uploadThemePhoto(context, photo, this);
    }


    public File getCameraTempFile() {
        return view.getCameraTempFile();
    }

    public Uri getBgPath() {
        return view.getBgPath();
    }
}
