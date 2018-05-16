package com.zhangteng.xim.bmob.http;

import android.text.TextUtils;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.FriendEntity;
import com.zhangteng.xim.bmob.entity.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMFileMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.MessageListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

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

    /**
     * 发送文本消息
     */
    public void sendMessage(String text, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        if (TextUtils.isEmpty(text.trim())) {
            return;
        }
        //TODO 发送消息：6.1、发送文本消息
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可随意设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        mConversationManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送本地图片文件
     */
    public void sendLocalImageMessage(String imgPath, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.2、发送本地图片消息
        //正常情况下，需要调用系统的图库或拍照功能获取到图片的本地地址，开发者只需要将本地的文件地址传过去就可以发送文件类型的消息
        BmobIMImageMessage image = new BmobIMImageMessage(imgPath);
        mConversationManager.sendMessage(image, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage(String imgUrl, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.3、发送远程图片消息
        BmobIMImageMessage image = new BmobIMImageMessage();
        image.setRemoteUrl(imgUrl);
        mConversationManager.sendMessage(image, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送本地音频文件
     */
    public void sendLocalAudioMessage(String audioPath, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        BmobIMAudioMessage audio = new BmobIMAudioMessage(audioPath);
        //TODO 发送消息：6.4、发送本地音频文件消息
        mConversationManager.sendMessage(audio, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送远程音频文件
     */
    public void sendRemoteAudioMessage(String audioUrl, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.5、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage();
        audio.setRemoteUrl(audioUrl);
        mConversationManager.sendMessage(audio, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送本地视频文件
     */
    public void sendLocalVideoMessage(String videoPath, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        BmobIMVideoMessage video = new BmobIMVideoMessage(videoPath);
        //TODO 发送消息：6.6、发送本地视频文件消息
        mConversationManager.sendMessage(video, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送远程视频文件
     */
    public void sendRemoteVideoMessage(String videoUrl, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.7、发送本地音频文件消息
        BmobIMAudioMessage audio = new BmobIMAudioMessage();
        audio.setRemoteUrl(videoUrl);
        mConversationManager.sendMessage(audio, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送本地文件
     */
    public void sendLocalFileMessage(String filePath, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.8、发送本地文件消息
        BmobIMFileMessage file = new BmobIMFileMessage(filePath);
        mConversationManager.sendMessage(file, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送远程文件
     */
    public void sendRemoteFileMessage(String fileUrl, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.9、发送远程文件消息
        BmobIMFileMessage file = new BmobIMFileMessage();
        file.setRemoteUrl(fileUrl);
        mConversationManager.sendMessage(file, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    /**
     * 发送地理位置消息
     */
    public void sendLocationMessage(String locationData, BmobIMConversation mConversationManager, final BmobCallBack<BmobIMMessage> bmobCallBack) {
        //TODO 发送消息：6.10、发送位置消息
        //测试数据，真实数据需要从地图SDK中获取
        BmobIMLocationMessage location = new BmobIMLocationMessage("广州番禺区", 23.5, 112.0);
        Map<String, Object> map = new HashMap<>();
        map.put("from", "百度地图");
        location.setExtraMap(map);
        mConversationManager.sendMessage(location, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                bmobCallBack.onResponse(bmobIMMessage, e);
            }
        });
    }

    ////////////////////////////////////////////自定义消息/////////////////////////////////////////////////////////////////
    /*
3.7.1、设置额外信息
有些时候，开发者需要在发送消息时携带一些额外信息，例如发送方的设备类型、图片的拍摄地点或者音频的来源等，那么开发者可以通过 BmobIMExtraMessage.extraMap属性来解决，任何继承BmobIMExtraMessage类的消息均支持设置额外信息。

    BmobIMAudioMessage audio =new BmobIMAudioMessage();
    image.setRemoteUrl("远程音频地址");
    //设置音频文件的来源
    Map<String,Object> map =new HashMap<>();
    map.put("from", "优酷");
    //TODO 自定义消息：7.1、给消息设置额外信息
    audio.setExtraMap(map);
    mConversationManager.sendMessage(audio, listener);
3.7.2、自定义消息类型
如果设置额外信息无法满足开发者的需求，那么开发者也可以自定义自己的消息类型。 1. 继承自BmobIMExtraMessage类； 2. 重写getMsgType方法，填写自定义的消息类型； 3. 重写isTransient方法，定义是否是暂态消息。

  //TODO 自定义消息：7.2、自定义消息类型，用于发送添加好友请求
    public class AddFriendMessage extends BmobIMExtraMessage{

        @Override
        public String getMsgType() {
            return "add";
        }

        @Override
        public boolean isTransient() {
            return true;
        }

        public AddFriendMessage(){}

    }
    */


    ////////////////////////////////////////////消息接收/////////////////////////////////////////////////

    /*
    3.8.1、自定义消息接收器
    3.8.1.1、自定义消息接收器继承自BmobIMMessageHandler来处理服务器发来的消息和离线消息。
       //TODO 消息接收：8.1、自定义全局消息接收器
      public class DemoMessageHandler extends BmobIMMessageHandler{

      private Context context;
       public DemoMessageHandler(Context context) {
           this.context = context;
       }

       @Override
        public void onMessageReceive(final MessageEvent event) {
            //接收处理在线消息
        }

       @Override
      public void onOfflineReceive(final OfflineMessageEvent event) {
          //接收处理离线消息，每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
       }
    }

    3.8.1.2、单个页面的自定义接收器
    使用IM SDK不仅可以使用BmobIMMessageHandler方式来注册全局的消息接收器，还可以使用MessageListHandler为单个页面注册消息接收器，具体步骤如下：

    在Activity/Fragment中实现MessageListHandler接口；
    在onResume方法中添加页面消息监听器：BmobIM.getInstance().addMessageListHandler(this)；
    在onPause方法中移除页面消息监听器：BmobIM.getInstance().removeMessageListHandler(this)；
    在MessageListHandler接口的onMessageReceive方法中做相关的操作。
    具体示例可查看NewIMDemo中的ChatActivity类：

    //TODO 消息接收：8.2、单个页面的自定义接收器
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //接收处理在线、离线消息
    }

    3.8.2、应用内消息接收
    SDK内部使用EventBus来进行应用内消息的分发，故在应用内需要接收消息的地方注册和解注册EventBus即可，不过SDK并没有集成EventBus，开发者需要在自己的项目中另外集成EventBus。

    SDK内部有两种EventBus事件：MessageEvent（在线消息）、OfflineMessageEvent(离线消息)。

    1、注册EventBus

    EventBus.getDefault().register(this);

    2、注销EventBus

    EventBus.getDefault().unregister(this);

    3、处理在线消息

    /**聊天消息接收事件
     * @param event
     /
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //处理聊天消息
    }

    4、处理离线消息


    /**离线消息接收事件
    * @param event
     /
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //处理离线消息
    }

    3.8.3、应用外通知栏提醒
    BmobNotificationManager类提供展示通知栏的方法，你也可以使用自己的展示通知栏方法。

            1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息。

            /**显示通知：多个用户的多条消息合并显示一条通知
             * @param event 某个消息事件：包含消息、会话及发送用户的信息
             * @param intent 跳转intent
             /
            //TODO 消息接收：8.5、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            BmobNotificationManager.getInstance(context).showNotification(MessageEvent event,Intent pendingIntent);
    2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息。

            /**显示通知
             * @param largerIcon 通知栏图标 开发者可传应用图标，也可以将聊天头像转成bitmap
             * @param title 标题
             * @param content 内容
             * @param ticker 状态栏上显示的内容
             * @param intent 跳转的intent
             /
            //TODO 消息接收：8.6、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
            BmobNotificationManager.getInstance(context).showNotification(Bitmap largerIcon,String title, String content, String ticker,Intent intent);
    */


    ////////////////////////////////////好友管理///////////////////////////////////////////////

    /**
     * 查询好友
     *
     * @param bmobCallBack
     */
    public void queryFriends(final BmobCallBack<List<FriendEntity>> bmobCallBack) {
        BmobQuery<FriendEntity> query = new BmobQuery<>();
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(new FindListener<FriendEntity>() {
            @Override
            public void done(List<FriendEntity> list, BmobException e) {
                bmobCallBack.onResponse(list, e);
            }
        });
    }

    /**
     * 删除好友
     *
     * @param f
     * @param bmobCallBack
     */
    public void deleteFriend(FriendEntity f, final BmobCallBack bmobCallBack) {
        FriendEntity friend = new FriendEntity();
        friend.delete(f.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                bmobCallBack.onResponse(null, e);
            }
        });
    }
}
