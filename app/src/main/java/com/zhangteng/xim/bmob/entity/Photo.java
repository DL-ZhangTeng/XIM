package com.zhangteng.xim.bmob.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by swing on 2018/6/4.
 */
public class Photo extends BmobObject {
    private User user;
    private String photo;
    private String mark;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
