package com.zhangteng.xim.bmob.entity;

import android.support.annotation.NonNull;

import com.zhangteng.swiperecyclerview.bean.GroupInfo;
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

    private GroupInfo groupInfo;

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

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
            char self = SortUtils.getFirstC(this.friendUser.getUsername());
            if (this.groupInfo == null) {
                this.groupInfo = new GroupInfo();
                groupInfo.setTitle(String.valueOf(self));
                groupInfo.setGroupNum(self);
                groupInfo.setPosition(GroupInfo.totals[self - 'A']);
                GroupInfo.totals[self - 'A']++;
            }
            char other = SortUtils.getFirstC(((Friend) o).getFriendUser().getUsername());
            if (((Friend) o).getGroupInfo() == null) {
                ((Friend) o).setGroupInfo(new GroupInfo());
                ((Friend) o).getGroupInfo().setPosition(GroupInfo.totals[other - 'A']);
                ((Friend) o).getGroupInfo().setTitle(String.valueOf(other));
                ((Friend) o).getGroupInfo().setGroupNum(other);
                GroupInfo.totals[other - 'A']++;
            }
            return self - other == 0 ? groupInfo.getPosition() - ((Friend) o).getGroupInfo().getPosition() : self - other;
        }
        throw new ClassCastException("only compare to friend");
    }
}