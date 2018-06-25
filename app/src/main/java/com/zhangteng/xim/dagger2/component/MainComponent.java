package com.zhangteng.xim.dagger2.component;

import com.zhangteng.xim.activity.MainActivity;
import com.zhangteng.xim.dagger2.base.BaseComponent;
import com.zhangteng.xim.dagger2.module.MainModule;

import dagger.Component;

@Component(modules = {MainModule.class}, dependencies = {BaseComponent.class})
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
