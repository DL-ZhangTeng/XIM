package com.zhangteng.xim.mvp.presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.zhangteng.xim.mvp.model.AboutXimModel;
import com.zhangteng.xim.mvp.view.AboutXimView;

/**
 * Created by swing on 2018/6/25.
 */
public class AboutXimPresenter {
    private AboutXimView aboutXimView;
    private AboutXimModel aboutXimModel;

    public AboutXimPresenter(AboutXimView aboutXimView, AboutXimModel aboutXimModel) {
        this.aboutXimView = aboutXimView;
        this.aboutXimModel = aboutXimModel;
    }

    public void updateVersion() {
        aboutXimModel.updateVersion(aboutXimView.getViewContext(), aboutXimView.getViewFragmentManger());
    }
}
