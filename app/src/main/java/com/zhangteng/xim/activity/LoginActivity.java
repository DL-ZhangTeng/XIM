package com.zhangteng.xim.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.LoginParams;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener {
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.bt_username_clear)
    Button username_clear;
    @BindView(R.id.bt_pwd_clear)
    Button password_clear;
    @BindView(R.id.bt_pwd_eye)
    Button password_eye;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.login_error)
    Button changepwd;

    @Override
    protected int getResourceId() {
        return R.layout.activity_login;
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
        password.setOnFocusChangeListener(this);
        username_clear.setOnClickListener(this);
        password_clear.setOnClickListener(this);
        password_eye.setOnTouchListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        changepwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_username_clear) {
            username.setText("");
        } else if (i == R.id.bt_pwd_clear) {
            password.setText("");
        } else if (i == R.id.login) {
            LoginParams loginParams = new LoginParams();
            if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                Toast.makeText(this, "username or password is null", Toast.LENGTH_SHORT).show();
            } else {
                loginParams.setName(username.getText().toString());
                loginParams.setPassword(password.getText().toString());
                UserApi.getInstance().login(loginParams, new BmobCallBack<User>(this, true) {
                    @Override
                    public void onSuccess(@Nullable User bmobObject) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();

                    }
                });
            }
        } else if (i == R.id.register) {
            LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 1);
        } else if (i == R.id.login_error) {
            LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, ChangePasswordActivity.class), 1);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int i = v.getId();
        if (i == R.id.username) {
            username_clear.setVisibility(View.VISIBLE);
            password_clear.setVisibility(View.INVISIBLE);

        } else if (i == R.id.password) {
            username_clear.setVisibility(View.INVISIBLE);
            password_clear.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case MotionEvent.ACTION_UP:
                password.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                //注册成功则返回用户名密码给登录界面的输入框
                username.setText(data.getStringExtra("username"));
                password.setText(data.getStringExtra("password"));
            }
        }
    }
}
