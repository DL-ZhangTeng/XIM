package com.zhangteng.xim.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;

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
