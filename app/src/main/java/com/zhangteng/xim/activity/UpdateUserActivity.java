package com.zhangteng.xim.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.UpdateUserParams;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.UserRefreshEvent;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class UpdateUserActivity extends BaseActivity {
    @BindView(R.id.updateuser_edittext)
    EditText editText;
    @BindView(R.id.updateuser_titlebar)
    TitleBar titleBar;

    private String uername;

    @Override
    protected int getResourceId() {
        return R.layout.activity_update_user;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        titleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UpdateUserParams updateUserParams = new UpdateUserParams();
                updateUserParams.setUsername(editText.getText().toString());

                UserApi.getInstance().updateUser(updateUserParams, new BmobCallBack(UpdateUserActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable Object bmobObject) {
                        LocalUser localUser = DBManager.instance(DBManager.USERNAME).queryUser(UserApi.getInstance().getUserInfo().getObjectId());
                        localUser.setUsername(updateUserParams.getUsername());
                        Intent intent = new Intent();
                        intent.putExtra("user", User.getUser(localUser));
                        UpdateUserActivity.this.setResult(2001, intent);
                        EventBus.getDefault().post(new UserRefreshEvent(User.getUser(localUser)));
                        AppManager.finishActivity(UpdateUserActivity.this);
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        UpdateUserActivity.this.showToast("修改失败");
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            uername = intent.getStringExtra("username");
            editText.setText(uername);
        }
    }

}
