package com.zhangteng.xim.bmob.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by swing on 2018/6/4.
 */
public class Like extends BmobObject {
    /**
     * 评论的动态
     */
    private Story story;
    /**
     * 评论人
     */
    private User user;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
