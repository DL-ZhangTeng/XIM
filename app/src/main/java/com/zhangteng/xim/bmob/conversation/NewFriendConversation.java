package com.zhangteng.xim.bmob.conversation;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zhangteng.xim.R;
import com.zhangteng.xim.activity.NewFriendActivity;
import com.zhangteng.xim.bmob.config.Config;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.NewFriend;

/**
 * 新朋友会话
 * Created by Administrator on 2016/5/25.
 */
public class NewFriendConversation extends Conversation {

    NewFriend lastFriend;

    public NewFriendConversation(NewFriend friend) {
        this.lastFriend = friend;
        this.cName = "新朋友";
    }

    @Override
    public String getLastMessageContent() {
        if (lastFriend != null) {
            Integer status = lastFriend.getStatus();
            String name = lastFriend.getName();
            if (TextUtils.isEmpty(name)) {
                name = lastFriend.getUid();
            }
            //目前的好友请求都是别人发给我的
            if (status == null || status == Config.STATUS_VERIFY_NONE || status == Config.STATUS_VERIFY_READED) {
                return name + "请求添加好友";
            } else {
                return "我已添加" + name;
            }
        } else {
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if (lastFriend != null) {
            return lastFriend.getTime();
        } else {
            return 0;
        }
    }

    @Override
    public Object getAvatar() {
        return R.mipmap.app_icon;
    }

    @Override
    public int getUnReadCount() {
        return DBManager.instance(DBManager.USERNAME).getNewInvitationCount();
    }

    @Override
    public void readAllMessages() {
        //批量更新未读未认证的消息为已读状态
        DBManager.instance(DBManager.USERNAME).updateBatchStatus();
    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NewFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(Context context) {
        DBManager.instance(DBManager.USERNAME).deleteNewFriend(lastFriend);
    }
}
