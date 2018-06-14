package com.zhangteng.xim.event;

import com.zhangteng.xim.bmob.entity.Story;

/**
 * Created by swing on 2018/6/14.
 */
public class CircleCommentEvent {
    private Story story;
    private int position;

    public CircleCommentEvent(Story story, int position) {
        this.story = story;
        this.position = position;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
