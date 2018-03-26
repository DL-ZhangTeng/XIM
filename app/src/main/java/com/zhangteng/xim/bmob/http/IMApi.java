package com.zhangteng.xim.bmob.http;

import android.text.TextUtils;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.UserEntity;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.MessageListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by swing on 2018/3/26.
 */

public class IMApi {

    private static IMApi instance;

    private IMApi() {
    }

    public static IMApi getInstance() {
        if (instance == null) {
            synchronized (IMApi.class) {
                instance = new IMApi();
            }
        }
        return instance;
    }
    /////////////////////////////////IM本地用户管理//////////////////////////////////

    /**
     * 用户管理：2.7、更新本地用户资料，用于在会话页面、聊天页面以及个人信息页面显示
     */
    public void updateUserInfo(Long id, String userId, String name, String avatar) {
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setId(id);
        info.setUserId(userId);
        info.setName(name);
        info.setAvatar(avatar);
        BmobIM.getInstance().updateUserInfo(info);
    }

    /**
     * 用户管理：2.8、批量更新本地用户信息
     */
    public void updateBatchUserInfo(List<BmobIMUserInfo> list) {
        BmobIM.getInstance().updateBatchUserInfo(list);
    }

    /**
     * 用户管理：2.9、获取本地用户信息
     */
    public BmobIMUserInfo getUserInfo(String uid) {
        return BmobIM.getInstance().getUserInfo(uid);
    }

    /////////////////////////////////连接IM服务器//////////////////////////////////

    /**
     * 连接IM服务器
     */
    public void connectService(final BmobCallBack<String> bmobCallBack) {
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    bmobCallBack.onResponse(uid, e);
                }
            });
        }
    }

    /**
     * 断开连接IM服务器
     */
    public void disConnectService() {
        BmobIM.getInstance().disConnect();
    }

    /**
     * 监听连接状态
     */
    public void setOnConnectStatusChangeListener(final BmobCallBack<ConnectionStatus> bmobCallBack) {
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                bmobCallBack.onResponse(status, null);
            }
        });
    }

    /**
     * get当前连接状态
     */
    public String getCurrentStatus() {
        return BmobIM.getInstance().getCurrentStatus().getMsg();
    }

    /////////////////////////////////会话相关//////////////////////////////////

    /**
     * 开启会话（暂态，常态）
     */
    public BmobIMConversation startPrivateConversation(BmobIMUserInfo info, boolean status) {
        return BmobIM.getInstance().startPrivateConversation(info, status, null);
    }

    /**
     * 查询全部会话
     */
    public List<BmobIMConversation> loadAllConversation() {
        return BmobIM.getInstance().loadAllConversation();
    }

    /**
     * 查询指定会话下的未读消息数
     */
    public long getUnReadCount(String conversationId) {
        return BmobIM.getInstance().getUnReadCount(conversationId);
    }

    /**
     * 全部会话下的未读消息数
     */
    public long getAllUnReadCount() {
        return BmobIM.getInstance().getAllUnReadCount();
    }

    /**
     * 删除指定会话
     */
    public void deleteConversation(String conversationId) {
        BmobIM.getInstance().deleteConversation(conversationId);
    }

    /**
     * 删除指定会话
     */
    public void deleteConversation(BmobIMConversation bmobIMConversation) {
        BmobIM.getInstance().deleteConversation(bmobIMConversation);
    }

    /**
     * 清空全部会话
     */
    public void clearAllConversation() {
        BmobIM.getInstance().clearAllConversation();
    }

    /**
     * 更新会话资料-如果消息是暂态消息，则不更新会话资料
     */
    public void updateConversation(BmobIMConversation conversation) {
        BmobIM.getInstance().updateConversation(conversation);
    }

    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param bmobCallBack
     */
    public void updateUserInfo(MessageEvent event, final BmobCallBack bmobCallBack) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        if (!username.equals(title)) {
            BmobQuery<UserEntity> query = new BmobQuery<UserEntity>();
            query.getObject(info.getUserId(), new QueryListener<UserEntity>() {

                @Override
                public void done(UserEntity object, BmobException e) {
                    if (e == null) {
                        String name = object.getUsername();
                        String avatar = object.getIcoPath();
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().updateUserInfo(info);
                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {

                    }
                    bmobCallBack.onResponse(null, e);
                }
            });

        } else {
            bmobCallBack.onResponse(null, null);
        }
    }


    /////////////////////////////////消息相关//////////////////////////////////

    /**
     * 获取消息管理
     */
    public BmobIMConversation getBmobIMConversation(BmobIMConversation conversationEntrance) {
        return BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
    }

    /**
     * 查询指定会话的聊天记录
     */
    public void queryMessages(BmobIMConversation mConversationManager, BmobIMMessage msg, int limit, final BmobCallBack<List<BmobIMMessage>> bmobCallBack) {
        mConversationManager.queryMessages(msg, limit, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    /**
     * 删除指定会话的聊天记录
     */
    public void deleteMessage(BmobIMConversation mConversationManager, BmobIMMessage msg) {
        mConversationManager.deleteMessage(msg);
    }

    /**
     * 删除指定会话的聊天记录（批量）
     */
    public void deleteBatchMessage(BmobIMConversation mConversationManager, List<BmobIMMessage> msgs) {
        mConversationManager.deleteBatchMessage(msgs);
    }

    /**
     * 清空指定会话的聊天记录（可以保留会话）
     */
    public void clearMessage(BmobIMConversation mConversationManager, boolean isKeepConversion, final BmobCallBack bmobCallBack) {
        mConversationManager.clearMessage(isKeepConversion, new MessageListener() {
            @Override
            public void done(BmobException e) {
                bmobCallBack.onResponse(null, e);
            }
        });
    }

    /**
     * 更新指定会话的所有消息为已读状态
     */
    public void updateLocalCache(BmobIMConversation mConversationManager) {
        mConversationManager.updateLocalCache();
    }

    /////////////////////////////////消息发送相关//////////////////////////////////

}
