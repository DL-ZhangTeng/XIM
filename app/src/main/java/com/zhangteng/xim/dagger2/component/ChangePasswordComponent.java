package com.zhangteng.xim.dagger2.component;

import com.zhangteng.xim.activity.ChangePasswordActivity;
import com.zhangteng.xim.dagger2.base.BaseComponent;
import com.zhangteng.xim.dagger2.module.ChangePasswordModule;

import dagger.Component;

@Component(modules = {ChangePasswordModule.class}, dependencies = {BaseComponent.class})
public interface ChangePasswordComponent {
    void inject(ChangePasswordActivity changePasswordActivity);
}
