package com.zhangteng.xim.bmob.entity;

import android.support.annotation.NonNull;

import com.zhangteng.xim.utils.SortUtils;

import cn.bmob.v3.BmobObject;

/**
 * Created by swing on 2018/5/16.
 */
public class Friend extends BmobObject implements Comparable {
    //用户
    private User user;
    //好友
    private User friendUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Friend) {
            return SortUtils.getFirstC(this.friendUser.getUsername()) - SortUtils.getFirstC(((Friend) o).getFriendUser().getUsername());
        }
        throw new ClassCastException("only compare to friend");
    }
}