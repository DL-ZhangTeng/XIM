package com.zhangteng.xim.dagger2.component;

import com.zhangteng.xim.activity.AboutXimActivity;
import com.zhangteng.xim.dagger2.module.AboutXimModule;

import dagger.Component;

/**
 * Created by swing on 2018/6/25.
 */
@Component(modules = {AboutXimModule.class}, dependencies = {BaseComponent.class})
public interface AboutXimComponent {
    void inject(AboutXimActivity aboutXimActivity);
}
