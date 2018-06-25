package com.zhangteng.xim.dagger2.module;

import com.zhangteng.xim.activity.ChangePasswordActivity;
import com.zhangteng.xim.mvp.model.ChangePasswordModel;
import com.zhangteng.xim.mvp.presenter.ChangePasswordPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangePasswordModule {
    private ChangePasswordActivity activity;
    private ChangePasswordPresenter presenter;
    private ChangePasswordModel model;

    public ChangePasswordModule(ChangePasswordActivity activity) {
        this.activity = activity;
        this.model = new ChangePasswordModel();
        this.presenter = new ChangePasswordPresenter(activity, model);
    }

    @Provides
    ChangePasswordPresenter ChangePasswordPresenter() {
        return presenter;
    }

    @Provides
    ChangePasswordModel ChangePasswordModel() {
        return model;
    }
}
