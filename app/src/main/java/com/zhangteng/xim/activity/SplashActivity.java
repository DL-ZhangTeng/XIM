package com.zhangteng.xim.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.androidpermission.AndroidPermission;
import com.zhangteng.androidpermission.Permission;
import com.zhangteng.androidpermission.callback.Callback;
import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.utils.AssetsUtils;

/**
 * Created by swing on 2018/5/24.
 */
public class SplashActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else if (msg.what == 999) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            SplashActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[Permission.Group.PHONE.length + Permission.Group.CAMERA.length + Permission.Group.STORAGE.length + Permission.Group.LOCATION.length + Permission.Group.MICROPHONE.length];
            System.arraycopy(Permission.Group.PHONE, 0, permissions, 0, Permission.Group.PHONE.length);
            System.arraycopy(Permission.Group.CAMERA, 0, permissions, Permission.Group.PHONE.length, Permission.Group.CAMERA.length);
            System.arraycopy(Permission.Group.STORAGE, 0, permissions, Permission.Group.PHONE.length + Permission.Group.CAMERA.length, Permission.Group.STORAGE.length);
            System.arraycopy(Permission.Group.LOCATION, 0, permissions, Permission.Group.PHONE.length + Permission.Group.CAMERA.length + Permission.Group.STORAGE.length, Permission.Group.LOCATION.length);
            System.arraycopy(Permission.Group.MICROPHONE, 0, permissions, Permission.Group.PHONE.length + Permission.Group.CAMERA.length + Permission.Group.STORAGE.length + Permission.Group.MICROPHONE.length, Permission.Group.MICROPHONE.length);
            new AndroidPermission.Buidler().permission(permissions).with(this).callback(new Callback() {
                @Override
                public void success() {

                }

                @Override
                public void failure() {

                }
            }).build();
        }
        //初始化地区数据库
        if (!AssetsUtils.isExistCityNoDb()) {
            AssetsUtils.initDatabase(AssetsUtils.dbName, MyApplication.getGlobalContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = UserApi.getInstance().getUserInfo();
        if (user != null && user.getObjectId() != null) {
            handler.sendEmptyMessageDelayed(999, 1000);
        } else {
            handler.sendEmptyMessageDelayed(1000, 1000);
        }
    }
}
