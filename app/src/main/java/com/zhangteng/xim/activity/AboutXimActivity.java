package com.zhangteng.xim.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.zhangteng.updateversionlibrary.UpdateVersion;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.http.UpdateVersionClient;

import butterknife.BindView;

public class AboutXimActivity extends BaseActivity {
    @BindView(R.id.about_version)
    TextView version;
    @BindView(R.id.about_version_update)
    TextView versionUpdate;

    @Override
    protected int getResourceId() {
        return R.layout.activity_about_xim;
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
        versionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateVersion.Builder()
                        //是否为调试模式
                        .isUpdateTest(false)
                        //通知栏显示
                        .isNotificationShow(false)
                        //是否自动安装
                        .isAutoInstall(true)
                        //获取服务器的版本信息
                        .isCheckUpdateCommonUrl("http://bmob-cdn-19421.b0.upaiyun.com/2018/06/20/c3096afd404e4cf28097a3bd68d0e03e.apk")
                        //是否提示更新信息
                        .isHintVersion(true)
                        //是否显示更新dialog
                        .isUpdateDialogShow(true)
                        //是否使用浏览器更新
                        .isUpdateDownloadWithBrowser(false)
                        .build()
                        //执行更新任务
                        .updateVersion(new UpdateVersionClient(AboutXimActivity.this, AboutXimActivity.this.getSupportFragmentManager()));
            }
        });
    }

    @Override
    protected void initData() {

    }
}
