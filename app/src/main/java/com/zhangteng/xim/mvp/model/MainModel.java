package com.zhangteng.xim.mvp.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.zhangteng.updateversionlibrary.UpdateVersion;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UpdateVersionClient;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.mvp.base.BaseModel;
import com.zhangteng.xim.mvp.presenter.MainPresenter;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobFile;
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
                .isHintVersion(false)
                //是否显示更新dialog
                .isUpdateDialogShow(true)
                //是否使用浏览器更新
                .isUpdateDownloadWithBrowser(false)
                .build()
                //执行更新任务
                .updateVersion(new UpdateVersionClient(context, fragmentManager));
    }

    public void sendAddFriendMessage(final Context context, String result, final MainPresenter mainPresenter) {
        UserApi.getInstance().queryUser(result, new BmobCallBack<User>(context, false) {
            @Override
            public void onSuccess(@Nullable User bmobObject) {
                BmobIMUserInfo userInfo = new BmobIMUserInfo();
                userInfo.setName(bmobObject.getUsername());
                userInfo.setAvatar(bmobObject.getIcoPath());
                userInfo.setUserId(bmobObject.getObjectId());
                IMApi.MassageSender.getInstance().sendAddFriendMessage(userInfo, new BmobCallBack<BmobIMMessage>(context, false) {
                    @Override
                    public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                        EventBus.getDefault().post(new RefreshEvent());
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        mainPresenter.showToast("发送请求失败");
                    }
                });
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                mainPresenter.showToast("解析二维码失败");
            }
        });
    }

    public void uploadThemePhoto(final Context context, final Photo photo, final MainPresenter mainPresenter) {
        BmobCallBack<String> bmobCallBack = new BmobCallBack<String>(context, true) {
            @Override
            public void onSuccess(@Nullable String bmobObject) {
                BmobFile bmobFile = new BmobFile(photo.getName(), null, bmobObject);
                bmobFile.setUrl(mainPresenter.getBgPath().getPath());
                photo.setPhoto(bmobFile);
                DataApi.getInstance().add(photo, new BmobCallBack<String>(context, false) {
                    @Override
                    public void onSuccess(@Nullable String bmobObject) {

                    }
                });
                mainPresenter.setHeaderViewBg(photo);
            }
        };
        bmobCallBack.onStart();
        DataApi.getInstance().uploadFile(mainPresenter.getCameraTempFile().getAbsolutePath(), bmobCallBack);
    }
}
