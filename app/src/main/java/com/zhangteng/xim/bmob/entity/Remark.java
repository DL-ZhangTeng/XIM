package com.zhangteng.xim.bmob.entity;

import cn.bmob.v3.BmobObject;

/**
 * 评论
 * Created by swing on 2018/6/4.
 */
public class Remark extends BmobObject {
    /**
     * 评论的动态
     */
    private Story story;
    /**
     * 回复的评论
     */
    private Remark remark;
    /**
     * 评论人
     */
    private User user;
    /**
     * 评论内容
     */
    private String content;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Remark getRemark() {
        return remark;
    }

    public void setRemark(Remark remark) {
        this.remark = remark;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
