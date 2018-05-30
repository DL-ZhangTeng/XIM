package com.zhangteng.xim.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.SendAdapter;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.SystemBarTintManager;
import com.zhangteng.xim.widget.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;

public class SendActivity extends AppCompatActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.send)
    ConstraintLayout send;
    private LocalUser user;
    private String objectId;
    private List<BmobIMConversation> list;
    private List<BmobIMMessage> data;
    private SendAdapter sendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint 激活导航栏
            tintManager.setNavigationBarTintEnabled(true);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(R.color.theme);
        }
        super.setContentView(getResourceId());
        AppManager.addActivity(this);
        setContentView(getResourceId());
        ButterKnife.bind(this);
        initView();
        initData();
    }


    protected int getResourceId() {
        return R.layout.activity_send;
    }


    protected void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra("objectId")) {
            objectId = getIntent().getStringExtra("objectId");
        }
        list = IMApi.ConversationManager.getInstance().loadAllConversation();
        for (BmobIMConversation bmobIMConversation : list) {
            if (bmobIMConversation.getConversationId().equals(objectId) || bmobIMConversation.getConversationTitle().equals(objectId)) {
                data = bmobIMConversation.getMessages();
            }
        }
        sendAdapter = new SendAdapter(this, data);
        recyclerView.setAdapter(sendAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobIMUserInfo bmobIMUserInfo = new BmobIMUserInfo();
                bmobIMUserInfo.setUserId(user.getObjectId());
                bmobIMUserInfo.setAvatar(user.getIcoPath());
                bmobIMUserInfo.setName(user.getUsername());
                IMApi.ConversationManager.getInstance().startPrivateConversation(bmobIMUserInfo, false, new BmobCallBack<BmobIMConversation>(SendActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable BmobIMConversation bmobIMConversation) {
                        IMApi.MassageSender.getInstance().sendMessage("111", bmobIMConversation, new BmobCallBack<BmobIMMessage>(SendActivity.this, false) {
                            @Override
                            public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                                Log.e("message", "success");
                            }

                            @Override
                            public void onFailure(BmobException bmobException) {
                                super.onFailure(bmobException);
                                Log.e("message", "failure");
                            }
                        });
                    }
                });
            }
        });
    }


    protected void initData() {
        user = DBManager.instance().queryUser(objectId);
        titleBar.setTitleText(user.getUsername());
    }


    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_left_back:
                goBack();
                break;
            default:
                break;
        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int mesageId) {
        Toast.makeText(this, mesageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            showToast("未找到相应应用");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void goBack() {
        ActivityHelper.setActivityAnimClose(this);
        AppManager.finishActivity(this);
    }
}
