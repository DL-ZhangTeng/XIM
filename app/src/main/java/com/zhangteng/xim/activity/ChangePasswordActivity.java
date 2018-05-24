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
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.LoginParams;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    @BindView(R.id.my_change_pwd_username)
    EditText username;
    @BindView(R.id.my_change_pwd_pwd)
    EditText password;
    @BindView(R.id.my_change_pwd_code)
    EditText code;
    @BindView(R.id.my_change_pwd_username_clear)
    Button username_clear;
    @BindView(R.id.my_change_pwd_pwd_clear)
    Button password_clear;
    @BindView(R.id.my_change_pwd_code_clear)
    Button code_clear;
    @BindView(R.id.my_change_pwd_code_get)
    Button code_get;
    @BindView(R.id.change_pwd)
    Button change_pwd;

    @Override
    protected int getResourceId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
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
        code_clear.setOnClickListener(this);
        code_get.setOnClickListener(this);
        change_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.my_change_pwd_username_clear) {
            username.setText("");
        } else if (i == R.id.my_change_pwd_pwd_clear) {
            password.setText("");
        } else if (i == R.id.my_change_pwd_code_clear) {
            code.setText("");
        } else if (i == R.id.my_change_pwd_code_get) {

        } else if (i == R.id.change_pwd) {
            if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                Toast.makeText(this, "username or password is null", Toast.LENGTH_SHORT).show();
            }
            LoginParams loginParams = new LoginParams();
            loginParams.setName(username.getText().toString());
            loginParams.setPassword(code.getText().toString());
            UserApi.getInstance().login(loginParams, new BmobCallBack(this, false) {
                @Override
                public void onSuccess(@Nullable Object bmobObject) {
                    UserApi.getInstance().resetPassword(code.getText().toString(), password.getText().toString(), new BmobCallBack(ChangePasswordActivity.this, true) {
                        @Override
                        public void onSuccess(@Nullable Object bmobObject) {
                            setResultFinish();
                        }

                        @Override
                        public void onFailure(BmobException bmobException) {
                            super.onFailure(bmobException);
                            Toast.makeText(ChangePasswordActivity.this, bmobException.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(BmobException bmobException) {
                    super.onFailure(bmobException);
                    Toast.makeText(ChangePasswordActivity.this, bmobException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setResultFinish() {
        Intent intent = new Intent();
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        setResult(1, intent);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int i = v.getId();
        if (i == R.id.my_change_pwd_username) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.VISIBLE);
                code_clear.setVisibility(View.INVISIBLE);
                password_clear.setVisibility(View.INVISIBLE);
            }

        } else if (i == R.id.my_change_pwd_code) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.INVISIBLE);
                code_clear.setVisibility(View.VISIBLE);
                password_clear.setVisibility(View.INVISIBLE);
            }

        } else if (i == R.id.my_change_pwd_pwd) {
            if (hasFocus == true) {
                username_clear.setVisibility(View.INVISIBLE);
                code_clear.setVisibility(View.INVISIBLE);
                password_clear.setVisibility(View.VISIBLE);
            }

        }
    }
}
