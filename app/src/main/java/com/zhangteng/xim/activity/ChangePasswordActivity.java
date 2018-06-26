package com.zhangteng.xim.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.dagger2.base.DaggerBaseComponent;
import com.zhangteng.xim.dagger2.component.DaggerChangePasswordComponent;
import com.zhangteng.xim.dagger2.module.ChangePasswordModule;
import com.zhangteng.xim.mvp.presenter.ChangePasswordPresenter;
import com.zhangteng.xim.mvp.view.ChangePasswordView;
import com.zhangteng.xim.utils.AppManager;

import javax.inject.Inject;

import butterknife.BindView;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, ChangePasswordView {

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
    @Inject
    ChangePasswordPresenter changePasswordPresenter;

    @Override
    protected int getResourceId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initInject() {
        DaggerChangePasswordComponent.builder()
                .changePasswordModule(new ChangePasswordModule(this))
                .baseComponent(DaggerBaseComponent.create())
                .build()
                .inject(this);
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
            changePasswordPresenter.clearUsername();
        } else if (i == R.id.my_change_pwd_pwd_clear) {
            changePasswordPresenter.clearPassword();
        } else if (i == R.id.my_change_pwd_code_clear) {
            changePasswordPresenter.clearCode();
        } else if (i == R.id.my_change_pwd_code_get) {

        } else if (i == R.id.change_pwd) {
            changePasswordPresenter.changePwd(this);
        }
    }

    @Override
    public void setResultFinish() {
        Intent intent = new Intent();
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        setResult(1, intent);
        AppManager.finishActivity(this);
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

    @Override
    public void setUserName(String userName) {
        this.username.setText(userName);
    }

    @Override
    public String getUserName() {
        return username.getText().toString();
    }

    @Override
    public void setPassword(String password) {
        this.password.setText(password);
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void setCode(String code) {
        this.code.setText(code);
    }

    @Override
    public String getCode() {
        return code.getText().toString();
    }


}
