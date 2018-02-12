package com.zhangteng.xim.bmob.http;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.UserEntity;
import com.zhangteng.xim.bmob.params.LoginParams;
import com.zhangteng.xim.bmob.params.RegisterParams;
import com.zhangteng.xim.bmob.params.UpdateUserParams;
import com.zhangteng.xim.bmob.params.UserParams;
import com.zhangteng.xim.bmob.tools.StringUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.zhangteng.xim.bmob.config.Config.SMSTEMPLATE;

/**
 * Created by swing on 2018/2/11.
 */

public class UserApi {
    private static UserApi instance;

    private UserApi() {
    }

    public static UserApi getInstance() {
        if (instance == null) {
            synchronized (UserApi.class) {
                instance = new UserApi();
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
     * 手机号+验证码注册并登录
     */
    public void signOrLoginByMobilePhone(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getPhone()) && StringUtils.isNotEmpty(params.getCode()))
            BmobUser.signOrLoginByMobilePhone("11位手机号码", "验证码", new LogInListener<UserEntity>() {

                @Override
                public void done(UserEntity user, BmobException e) {
                    callBack.onResponse(user, e);
                }
            });
    }

    /**
     * 手机+验证码注册并登录（可填写用户信息）
     */
    public void signOrLogin(UpdateUserParams params, final BmobCallBack callBack) {
        UserEntity userInfo = new UserEntity();
        if (null != params.getAge())
            userInfo.setAge(params.getAge());
        if (StringUtils.isNotEmpty(params.getPhone()))
            userInfo.setMobilePhoneNumber(params.getPhone());//设置手机号码（必填）
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
        if (StringUtils.isNotEmpty(params.getPassword()))//设置用户密码
            userInfo.setPassword(params.getPassword());
        if (StringUtils.isNotEmpty(params.getEmail()))
            userInfo.setEmail(params.getEmail());
        if (StringUtils.isNotEmpty(params.getName()))
            userInfo.setUsername(params.getName());//设置用户名，如果没有传用户名，则默认为手机号码
        if (StringUtils.isNotEmpty(params.getCode()))
            userInfo.signOrLogin(params.getCode(), new SaveListener<UserEntity>() {

                @Override
                public void done(UserEntity user, BmobException e) {
                    callBack.onResponse(user, e);
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

    /**
     * 邮箱+密码 手机号+密码 登录
     */
    public void loginByAccount(LoginParams params, final BmobCallBack callBack) {
        String account = null;
        if (StringUtils.isNotEmpty(params.getEmail()))
            account = params.getEmail();
        if (StringUtils.isNotEmpty(params.getPhone()))
            account = params.getPhone();
        BmobUser.loginByAccount(account, params.getPassword(), new LogInListener<UserEntity>() {

            @Override
            public void done(UserEntity user, BmobException e) {
                callBack.onResponse(user, e);
            }
        });
    }

    /**
     * 手机号+验证码登录
     */
    public void loginBySMSCode(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getPhone()) && StringUtils.isNotEmpty(params.getCode()))
            BmobUser.loginBySMSCode("11位手机号码", params.getCode(), new LogInListener<UserEntity>() {

                @Override
                public void done(UserEntity user, BmobException e) {
                    callBack.onResponse(user, e);
                }
            });
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
     * 退出登录
     */
    public void Logout() {
        BmobUser.logOut();   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
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
     * 重设密码
     */
    public void resetPassword(final BmobCallBack callBack) {
        BmobUser.updateCurrentUserPassword("旧密码", "新密码", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                callBack.onResponse(null, e);
            }

        });
    }

    /**
     * 请求邮箱重置密码
     */
    public void resetPasswordByEmail(LoginParams params, final BmobCallBack callBack) {
        BmobUser.resetPasswordByEmail(params.getEmail(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                callBack.onResponse(null, e);
            }
        });
    }

    /**
     * 重设密码（验证码）
     */
    public void resetPasswordBySMSCode(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getCode()) && StringUtils.isNotEmpty(params.getPassword()))
            BmobUser.resetPasswordBySMSCode(params.getCode(), params.getPassword(), new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    callBack.onResponse(null, ex);
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
        if (StringUtils.isNotEmpty(params.getPassword()))
            userInfo.setPassword(params.getPassword());
        if (StringUtils.isNotEmpty(params.getEmail()))
            userInfo.setEmail(params.getEmail());
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
     * 请求邮箱验证
     */
    public void requestEmailVerify(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getEmail()))
            BmobUser.requestEmailVerify(params.getEmail(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    callBack.onResponse(null, e);
                }
            });
    }

    /**
     * 请求手机验证码
     */
    public void requestSMSCode(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getPhone()))
            BmobSMS.requestSMSCode(params.getPhone(), SMSTEMPLATE, new QueryListener<Integer>() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    callBack.onResponse(smsId, ex);
                }
            });
    }

    /**
     * 验证验证码是否正确
     */
    public void verifySmsCode(LoginParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getPhone()) && StringUtils.isNotEmpty(params.getCode()))
            BmobSMS.verifySmsCode(params.getPhone(), params.getCode(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    callBack.onResponse(null, e);
                }
            });
    }

    /**
     * 绑定手机号（先验证手机号码）
     */
    public void bindPhone(UserParams params, final BmobCallBack callBack) {
        if (StringUtils.isNotEmpty(params.getPhone())) {
            UserEntity user = new UserEntity();
            user.setMobilePhoneNumber(params.getPhone());
            user.setMobilePhoneNumberVerified(true);
            UserEntity cur = BmobUser.getCurrentUser(UserEntity.class);
            user.update(cur.getObjectId(), new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    callBack.onResponse(null, e);
                }
            });
        }
    }
}
