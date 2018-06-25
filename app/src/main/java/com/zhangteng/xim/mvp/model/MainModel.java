package com.zhangteng.xim.mvp.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zhangteng.xim.R;
import com.zhangteng.xim.activity.MainActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.mvp.base.BaseModel;
import com.zhangteng.xim.mvp.presenter.MainPresenter;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.exception.BmobException;

public class MainModel extends BaseModel {
    private User user;

    public void connectService(Context context, final MainPresenter mainPresenter) {
        IMApi.IMServiceManager.getInstance().connectService(new BmobCallBack<String>(context, false) {
            @Override
            public void onSuccess(@Nullable String bmobObject) {
                IMApi.LoacalUserManager.getInstance()
                        .updateUserInfo(
                                UserApi.getInstance().getUserInfo().getObjectId()
                                , TextUtils.isEmpty(
                                        UserApi.getInstance().getUserInfo().getRealName())
                                        ? UserApi.getInstance().getUserInfo().getUsername()
                                        : UserApi.getInstance().getUserInfo().getRealName()
                                , UserApi.getInstance().getUserInfo().getIcoPath()
                        );
                EventBus.getDefault().post(new RefreshEvent());
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                mainPresenter.showToast(bmobException.getMessage());
            }
        });
    }

    public void queryHeaderViewBg(Context context, final MainPresenter mainPresenter) {
        DataApi.getInstance().queryThemePhoto(getUser(), new BmobCallBack<Photo>(context, false) {
            @Override
            public void onSuccess(@Nullable Photo bmobObject) {
                mainPresenter.setHeaderViewBg(bmobObject);
            }
        });
    }

    public User getUser() {
        return user == null ? user = UserApi.getInstance().getUserInfo() : user;
    }
}
