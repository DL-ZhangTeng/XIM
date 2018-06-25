package com.zhangteng.xim.dagger2.module;

import com.zhangteng.xim.activity.AboutXimActivity;
import com.zhangteng.xim.mvp.model.AboutXimModel;
import com.zhangteng.xim.mvp.presenter.AboutXimPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by swing on 2018/6/25.
 */
@Module
public class AboutXimModule {
    private AboutXimActivity activity;
    private AboutXimPresenter presenter;
    private AboutXimModel model;

    public AboutXimModule(AboutXimActivity activity) {
        this.activity = activity;
        this.model = new AboutXimModel();
        this.presenter = new AboutXimPresenter(activity, model);
    }

    @Provides
    AboutXimPresenter aboutXimPresenter() {
        return presenter;
    }

    @Provides
    AboutXimModel aboutXimModel() {
        return model;
    }
}
