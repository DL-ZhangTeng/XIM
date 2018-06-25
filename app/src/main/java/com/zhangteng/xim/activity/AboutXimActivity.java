package com.zhangteng.xim.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.zhangteng.updateversionlibrary.UpdateVersion;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.http.UpdateVersionClient;
import com.zhangteng.xim.dagger2.component.DaggerAboutXimComponent;
import com.zhangteng.xim.dagger2.component.DaggerBaseComponent;
import com.zhangteng.xim.dagger2.module.AboutXimModule;
import com.zhangteng.xim.mvp.presenter.AboutXimPresenter;
import com.zhangteng.xim.mvp.view.AboutXimView;

import javax.inject.Inject;

import butterknife.BindView;

public class AboutXimActivity extends BaseActivity implements View.OnClickListener, AboutXimView {
    @BindView(R.id.about_version)
    TextView version;
    @BindView(R.id.about_version_update)
    TextView versionUpdate;
    @Inject
    AboutXimPresenter presenter;

    @Override
    protected int getResourceId() {
        return R.layout.activity_about_xim;
    }

    @Override
    protected void initInject() {
        DaggerAboutXimComponent.builder()
                .aboutXimModule(new AboutXimModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version.setText(String.format("V %s", info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionUpdate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_version_update:
                presenter.updateVersion();
                break;
            default:
                break;
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public FragmentManager getViewFragmentManger() {
        return getSupportFragmentManager();
    }
}
