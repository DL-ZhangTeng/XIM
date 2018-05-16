package com.zhangteng.xim.bmob.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by swing on 2018/5/16.
 */
public class FriendEntity extends BmobObject {
    //用户
    private UserEntity user;
    //好友
    private UserEntity friendUser;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(UserEntity friendUser) {
        this.friendUser = friendUser;
    }
}