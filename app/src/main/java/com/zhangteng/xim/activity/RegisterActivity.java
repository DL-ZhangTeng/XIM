package com.zhangteng.xim.activity;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.RegisterParams;
import com.zhangteng.xim.utils.AppManager;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    @BindView(R.id.my_register_username)
    EditText username;
    @BindView(R.id.my_register_pwd)
    EditText password;
    @BindView(R.id.my_register_code)
    EditText code;
    @BindView(R.id.my_register_username_clear)
    Button username_clear;
    @BindView(R.id.my_register_pwd_clear)
    Button password_clear;
    @BindView(R.id.my_register_code_clear)
    Button password_clear_;
    @BindView(R.id.my_register_code_get)
    Button code_get;
    @BindView(R.id.register)
    Button register;

    @Override
    protected int getResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        initClicked();
    }

    @Override
    protected void initData() {

    }

    public void initClicked() {
        username.setOnFocusChangeListener(this);
        code.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        username_clear.setOnClickListener(this);
        password_clear.setOnClickListener(this);
        password_clear_.setOnClickListener(this);
        code_get.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.my_register_username_clear) {
            username.setText("");
        } else if (i == R.id.my_register_pwd_clear) {
            password.setText("");
        } else if (i == R.id.my_register_code_clear) {
            code.setText("");
        } else if (i == R.id.my_register_code_get) {

        } else if (i == R.id.register) {
            if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                Toast.makeText(this, "username or password is null", Toast.LENGTH_SHORT).show();
            } else if (!password.getText().toString().equals(code.getText().toString())) {
                Toast.makeText(this, "Verify password failed", Toast.LENGTH_SHORT).show();
            } else {
                RegisterParams registerParams = new RegisterParams();
                registerParams.setName(username.getText().toString());
                registerParams.setPassword(password.getText().toString());
                UserApi.getInstance().register(registerParams, new BmobCallBack<User>(this, true) {
                    @Override
                    public void onSuccess(@Nullable User bmobObject) {
                        Friend friend = new Friend();
                        friend.setUser(bmobObject);
                        friend.setFriendUser(bmobObject);
                        IMApi.FriendManager.getInstance().addFriend(friend, new BmobCallBack<String>(RegisterActivity.this, false) {
                            @Override
                            public void onSuccess(@Nullable String bmobObject) {

                            }
                        });
                        setResultFinish();
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        Toast.makeText(RegisterActivity.this, bmobException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void setResultFinish() {
        Intent intent = new Intent();
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        RegisterActivity.this.setResult(1, intent);
        AppManager.finishActivity(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int i = v.getId();
        if (i == R.id.my_register_username) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.VISIBLE);
                password_clear_.setVisibility(View.INVISIBLE);
                password_clear.setVisibility(View.INVISIBLE);
            }

        } else if (i == R.id.my_register_code) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.INVISIBLE);
                password_clear_.setVisibility(View.VISIBLE);
                password_clear.setVisibility(View.INVISIBLE);
            }

        } else if (i == R.id.my_register_pwd) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.INVISIBLE);
                password_clear_.setVisibility(View.INVISIBLE);
                password_clear.setVisibility(View.VISIBLE);
            }

        }
    }
}
