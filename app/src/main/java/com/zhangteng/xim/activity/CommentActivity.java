package com.zhangteng.xim.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.event.CircleCommentEvent;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.SoftInputUtils;
import com.zhangteng.xim.utils.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.send)
    ConstraintLayout send;
    @BindView(R.id.edit_msg)
    EditText editText;
    @BindView(R.id.btn_send)
    Button button;

    private Story story;
    private Remark remark;
    private int position;

    protected int getResourceId() {
        return R.layout.activity_comment;
    }

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
        EventBus.getDefault().register(this);
        AppManager.addActivity(this);
        setContentView(getResourceId());
        ButterKnife.bind(this);
        initInject();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {

    }

    protected void initInject() {

    }

    protected void initView() {
        Bundle bundle = getBundle();
        if (bundle.containsKey("story")) {
            story = (Story) bundle.getSerializable("story");
        }
        if (bundle.containsKey("remark")) {
            remark = (Remark) bundle.getSerializable("remark");
        }
        if (bundle.containsKey("position")) {
            position = bundle.getInt("position");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftInputUtils.closeKeybord(editText, CommentActivity.this);
                final Remark nremark = new Remark();
                nremark.setUser(UserApi.getInstance().getUserInfo());
                nremark.setStory(story);
                nremark.setRemark(remark);
                nremark.setContent(editText.getText().toString());
                DataApi.getInstance().add(nremark, new BmobCallBack<String>(CommentActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable String bmobObject) {
                        nremark.setObjectId(bmobObject);
                        if (story.getRemarks() == null) {
                            story.setRemarks(new ArrayList<Remark>());
                        }
                        story.getRemarks().add(nremark);
                        EventBus.getDefault().post(new CircleCommentEvent(story, position));
                        goBack();
                    }
                });
            }
        });
    }


    protected void initData() {

    }

    public Bundle getBundle() {
        if (getIntent() != null) {
            return getIntent().getExtras();
        } else {
            return new Bundle();
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
        overridePendingTransition(0, 0);
    }

}
