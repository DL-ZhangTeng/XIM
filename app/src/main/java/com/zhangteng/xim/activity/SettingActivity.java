package com.zhangteng.xim.activity;

import android.view.View;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;

import cn.bmob.newim.core.ConnectionStatus;

public class SettingActivity extends BaseActivity {

    @Override
    protected int getResourceId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void goBack() {
        super.goBack();
        finish();
    }

    @Override
    public void buttonClick(View v) {
        super.buttonClick(v);
        switch (v.getId()) {
            case R.id.setting_logout:
                if (IMApi.IMServiceManager.getInstance().getCurrentStatus().getCode() == ConnectionStatus.CONNECTED.getCode()) {
                    IMApi.IMServiceManager.getInstance().disConnectService();
                }
                UserApi.getInstance().Logout();
                AppManager.finishActivity(this);
                ActivityHelper.jumpActivity(SettingActivity.this, LoginActivity.class, 1);
                break;
            case R.id.setting_about:
                break;
            case R.id.setting_change:
                break;
            case R.id.setting_feedback:
                break;
            default:
                break;
        }
    }
}
