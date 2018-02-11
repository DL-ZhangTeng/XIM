package com.zhangteng.xim.bmob.http;

import android.widget.Toast;

import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.UserEntity;
import com.zhangteng.xim.bmob.params.LoginParams;
import com.zhangteng.xim.bmob.params.RegisterParams;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.utils.ToastUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by swing on 2018/2/11.
 */

public class Api {
    private static Api instance;

    private Api() {
    }

    public static Api getInstance() {
        if (instance == null) {
            synchronized (Api.class) {
                instance = new Api();
            }
        }
        return instance;
    }

    /**
     * 注册
     *
     * @param params
     * @param callBack
     */
    public static void register(RegisterParams params, final BmobCallBack callBack) {
        UserEntity bu = new UserEntity();
        if (StringUtils.isNotEmpty(params.getName()))
            bu.setUsername(params.getName());
        if (StringUtils.isNotEmpty(params.getPassword()))
            bu.setPassword(params.getPassword());
        if (StringUtils.isNotEmpty(params.getEmail()))
            bu.setEmail(params.getEmail());
        if (StringUtils.isNotEmpty(params.getPhone()))
            bu.setMobilePhoneNumber(params.getPhone());
        bu.signUp(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity s, BmobException e) {
                if (e == null) {
                    ToastUtils.show(MyApplication.getGlobalContext(), "注册成功", Toast.LENGTH_SHORT);
                    callBack.onSuccess(s);
                } else {
                    callBack.onFailure(e);
                    ToastUtils.show(MyApplication.getGlobalContext(), "注册失败", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * 登录
     *
     * @param params
     * @param callBack
     */
    public static void login(LoginParams params, BmobCallBack callBack) {
        UserEntity bu = new UserEntity();
        if (StringUtils.isNotEmpty(params.getName()))
            bu.setUsername(params.getName());
        if (StringUtils.isNotEmpty(params.getPassword()))
            bu.setPassword(params.getPassword());
        login(bu, callBack);
    }

    public static void login(final UserEntity userEntity, final BmobCallBack callBack) {
        userEntity.login(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity bmobUser, BmobException e) {
                if (e == null) {
                    callBack.onSuccess(userEntity);
                    ToastUtils.show(MyApplication.getGlobalContext(), "登录成功", Toast.LENGTH_SHORT);
                } else {
                    callBack.onFailure(e);
                    ToastUtils.show(MyApplication.getGlobalContext(), "登录失败", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * 获取本地缓存用户信息
     */
    public static UserEntity getUserInfo() {
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        return user;
    }

    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     *
     * @see cn.bmob.v3.helper.ErrorCode#E9024S
     */
    private void fetchUserInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }
}
