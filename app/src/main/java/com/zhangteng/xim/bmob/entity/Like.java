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
    private User from;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }
}
