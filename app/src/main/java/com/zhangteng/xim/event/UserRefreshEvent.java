package com.zhangteng.xim.event;

import com.zhangteng.xim.bmob.entity.User;

/**
 * Created by swing on 2018/6/28.
 */
public class UserRefreshEvent {
    private User user;

    public UserRefreshEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
