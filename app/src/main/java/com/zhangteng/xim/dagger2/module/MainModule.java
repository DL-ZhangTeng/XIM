package com.zhangteng.xim.dagger2.module;

import com.zhangteng.xim.activity.MainActivity;
import com.zhangteng.xim.adapter.MainAdapter;
import com.zhangteng.xim.mvp.model.MainModel;
import com.zhangteng.xim.mvp.presenter.MainPresenter;
import com.zhangteng.xim.widget.DropDownMenu;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private MainActivity activity;
    private MainPresenter presenter;
    private MainModel model;

    public MainModule(MainActivity activity) {
        this.activity = activity;
        this.model = new MainModel();
        this.presenter = new MainPresenter(activity, model);
    }

    @Provides
    MainPresenter MainPresenter() {
        return presenter;
    }

    @Provides
    MainModel MainModel() {
        return model;
    }

    @Provides
    MainAdapter MainAdapter() {
        return new MainAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    DropDownMenu DropDownMenu() {
        return new DropDownMenu(activity);
    }
}
