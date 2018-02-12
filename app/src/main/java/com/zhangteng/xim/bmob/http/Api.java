package com.zhangteng.xim.bmob.http;

import android.widget.Toast;

import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.UserEntity;
import com.zhangteng.xim.bmob.params.LoginParams;
import com.zhangteng.xim.bmob.params.RegisterParams;
import com.zhangteng.xim.bmob.params.UpdateUserParams;
import com.zhangteng.xim.bmob.params.UserParams;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    public void register(RegisterParams params, final BmobCallBack callBack) {
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
                callBack.onResponse(s, e);
            }
        });
    }

    /**
     * 登录
     *
     * @param params
     * @param callBack
     */
    public void login(LoginParams params, BmobCallBack callBack) {
        UserEntity bu = new UserEntity();
        if (StringUtils.isNotEmpty(params.getName()))
            bu.setUsername(params.getName());
        if (StringUtils.isNotEmpty(params.getPassword()))
            bu.setPassword(params.getPassword());
        login(bu, callBack);
    }

    public void login(final UserEntity userEntity, final BmobCallBack callBack) {
        userEntity.login(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity bmobUser, BmobException e) {
                callBack.onResponse(bmobUser, e);
            }
        });
    }

    /**
     * 获取本地缓存用户信息
     */
    public UserEntity getUserInfo() {
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        return user;
    }

    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     *
     * @see cn.bmob.v3.helper.ErrorCode#E9024S
     */
    public void fetchUserInfo(final BmobCallBack callBack) {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                callBack.onResponse(s, e);
            }
        });
    }

    /**
     * 修改用户信息
     */
    public void updateUser(UpdateUserParams params, final BmobCallBack callBack) {
        UserEntity userInfo = new UserEntity();
        if (null != params.getAge())
            userInfo.setAge(params.getAge());
        if (StringUtils.isNotEmpty(params.getPhone()))
            userInfo.setMobilePhoneNumber(params.getPhone());
        if (params.getAreaId() != null)
            userInfo.setAreaId(params.getAreaId());
        if (params.getCityId() != null)
            userInfo.setCityId(params.getCityId());
        if (params.getClassId() != null)
            userInfo.setClassId(params.getClassId());
        if (params.getGradeId() != null)
            userInfo.setGradeId(params.getGradeId());
        if (StringUtils.isNotEmpty(params.getIcoPath()))
            userInfo.setIcoPath(params.getIcoPath());
        if (params.getProvinceId() != null)
            userInfo.setProvinceId(params.getProvinceId());
        if (StringUtils.isNotEmpty(params.getRealName()))
            userInfo.setRealName(params.getRealName());
        if (params.getRoleId() != null)
            userInfo.setRoleId(params.getRoleId());
        if (params.getSchoolId() != null)
            userInfo.setSchoolId(params.getSchoolId());
        if (params.getSex() != null)
            userInfo.setSex(params.getSex());
        UserEntity currentUser = getUserInfo();
        userInfo.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                callBack.onResponse(null, e);
            }
        });
    }

    /**
     * 查询用户
     */
    public void queryUser(UserParams params, final BmobCallBack callBack) {
        BmobQuery<UserEntity> query = new BmobQuery<UserEntity>();
        if (StringUtils.isNotEmpty(params.getName()))
            query.addWhereEqualTo("username", params.getName());
        if (StringUtils.isNotEmpty(params.getEmail()))
            query.addWhereEqualTo("email", params.getEmail());
        if (StringUtils.isNotEmpty(params.getPhone()))
            query.addWhereEqualTo("mobilePhoneNumber", params.getPhone());
        query.findObjects(new FindListener<UserEntity>() {
            @Override
            public void done(List<UserEntity> object, BmobException e) {
                callBack.onResponse(object, e);
            }
        });
    }

    /**
     * 退出登录
     */
    public void Logout() {
        BmobUser.logOut();   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
    }
}
