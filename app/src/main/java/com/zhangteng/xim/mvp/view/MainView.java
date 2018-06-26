package com.zhangteng.xim.mvp.view;

import android.net.Uri;

import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.mvp.base.BaseView;

import java.io.File;


public interface MainView extends BaseView {
    void showToast(String toast);

    void setHeaderViewBg(Photo photo);

    void setUsername(String username);

    void setAvatar(String iconPath);

    void setTitleBarLeftIcon(String iconPath);

    File getCameraTempFile();

    Uri getBgPath();
}
