package com.zhangteng.xim.mvp.presenter;

import com.zhangteng.xim.mvp.base.BasePresenter;
import com.zhangteng.xim.mvp.model.AboutXimModel;
import com.zhangteng.xim.mvp.view.AboutXimView;

/**
 * Created by swing on 2018/6/25.
 */
public class AboutXimPresenter extends BasePresenter{
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
