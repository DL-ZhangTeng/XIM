package com.zhangteng.xim.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.androidpermission.request.MRequest;
import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.event.JumpEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.AssetsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by swing on 2018/5/24.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.activity_splash);
        AppManager.addActivity(this);
        EventBus.getDefault().register(this);
        initInject();
        //初始化地区数据库
        if (!AssetsUtils.isExistCityNoDb()) {
            AssetsUtils.initDatabase(AssetsUtils.dbName, MyApplication.getGlobalContext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Permission.READ_PHONE_STATE,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.CAMERA,
                    Permission.RECORD_AUDIO};
            new AndroidPermission.Buidler()
                    .with(this)
                    .request(new MRequest(permissions) {
                        @Override
                        public void requestPermissions(Context context, int permissionCode, Callback callback) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                super.requestPermissions(context, permissionCode, callback);
                                overridePendingTransition(0, 0);
                            }
                        }
                    })
                    .callback(new Callback() {
                        @Override
                        public void success() {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new JumpEvent(SplashActivity.this));
                                }
                            }, 2000);
                        }

                        @Override
                        public void failure() {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new JumpEvent(SplashActivity.this));
                                }
                            }, 2000);
                        }
                    })
                    .permission(permissions)
                    .build()
                    .excute();
        } else {
            EventBus.getDefault().post(new JumpEvent(SplashActivity.this));
        }
    }

    protected void initInject() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Subscribe
    public void onEventMainThread(JumpEvent event) {
        if (event.getActivity() instanceof SplashActivity) {
            User user = UserApi.getInstance().getUserInfo();
            if (user != null && user.getObjectId() != null) {
                ActivityHelper.jumpActivity(SplashActivity.this, MainActivity.class, 1);
            } else {
                ActivityHelper.jumpActivity(SplashActivity.this, LoginActivity.class, 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
