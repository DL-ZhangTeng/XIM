package com.zhangteng.xim.mvp.model;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.zhangteng.updateversionlibrary.UpdateVersion;
import com.zhangteng.xim.bmob.http.UpdateVersionClient;

/**
 * Created by swing on 2018/6/25.
 */
public class AboutXimModel {

    public void updateVersion(Context context, FragmentManager fragmentManager) {
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
                .updateVersion(new UpdateVersionClient(context, fragmentManager));

    }
}
