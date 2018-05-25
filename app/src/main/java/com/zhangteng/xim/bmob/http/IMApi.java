package com.zhangteng.xim.bmob.http;

import android.text.TextUtils;

import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.config.Config;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.message.AddFriendMessage;
import com.zhangteng.xim.bmob.message.AgreeAddFriendMessage;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.NewFriend;

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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by swing on 2018/3/26.
 */

public class IMApi {

    /////////////////////////////////IM本地用户管理/////////////////////////////
    public static class LoacalUserManager {
        private static LoacalUserManager instance;

        private LoacalUserManager() {
        }

        public static LoacalUserManager getInstance() {
            if (instance == null) {
                synchronized (LoacalUserManager.class) {
                    instance = new LoacalUserManager();
                }
            }
            return instance;
        }

        /**
         * 用户管理：2.7、更新本地用户资料，用于在会话页面、聊天页面以及个人信息页面显示
         */
        public void updateUserInfo(String userId, String name, String avatar) {
            BmobIMUserInfo info = new BmobIMUserInfo();
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

    }

    /////////////////////////////////连接IM服务器///////////////////////////////
    public static class IMServiceManager {
        private static IMServiceManager instance;

        private IMServiceManager() {
        }

        public static IMServiceManager getInstance() {
            if (instance == null) {
                synchronized (IMServiceManager.class) {
                    instance = new IMServiceManager();
                }
            }
            return instance;
        }

        /**
         * 连接IM服务器
         */
        public void connectService(final BmobCallBack<String> bmobCallBack) {
            User user = BmobUser.getCurrentUser(User.class);
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
        public ConnectionStatus getCurrentStatus() {
            return BmobIM.getInstance().getCurrentStatus();
        }

    }

    /////////////////////////////////会话相关//////////////////////////////////
    public static class ConversationManager {
        private static ConversationManager instance;

        private ConversationManager() {
        }

        public static ConversationManager getInstance() {
            if (instance == null) {
                synchronized (ConversationManager.class) {
                    instance = new ConversationManager();
                }
            }
            return instance;
        }

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
                BmobQuery<User> query = new BmobQuery<User>();
                query.getObject(info.getUserId(), new QueryListener<User>() {

                    @Override
                    public void done(User object, BmobException e) {
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
    }

    /////////////////////////////////消息相关//////////////////////////////////
    public static class MessageManager {
        private static MessageManager instance;

        private MessageManager() {
        }

        public static MessageManager getInstance() {
            if (instance == null) {
                synchronized (MessageManager.class) {
                    instance = new MessageManager();
                }
            }
            return instance;
        }

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
    }

    /////////////////////////////////消息发送相关//////////////////////////////
    public static class MassageSender {
        private static MassageSender instance;

        private MassageSender() {
        }

        public static MassageSender getInstance() {
            if (instance == null) {
                synchronized (MassageSender.class) {
                    instance = new MassageSender();
                }
            }
            return instance;
        }

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

        /**
         * 发送添加好友的请求
         */
        //TODO 好友管理：9.7、发送添加好友请求
        private void sendAddFriendMessage(BmobIMUserInfo info, final BmobCallBack<BmobIMMessage> bmobCallBack) {
            //TODO 会话：4.1、创建一个暂态会话入口，发送好友请求
            BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
            //TODO 消息：5.1、根据会话入口获取消息管理，发送好友请求
            BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
            AddFriendMessage msg = new AddFriendMessage();
            User currentUser = BmobUser.getCurrentUser(User.class);
            msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
            //TODO 这里只是举个例子，其实可以不需要传发送者的信息过去
            Map<String, Object> map = new HashMap<>();
            map.put("name", currentUser.getUsername());//发送者姓名
            map.put("avatar", currentUser.getIcoPath());//发送者的头像
            map.put("uid", currentUser.getObjectId());//发送者的uid
            msg.setExtraMap(map);
            messageManager.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    bmobCallBack.onResponse(msg, e);
                }
            });
        }

        /**
         * 发送同意添加好友的消息
         */
        //TODO 好友管理：9.8、发送同意添加好友
        private void sendAgreeAddFriendMessage(final NewFriend add, final BmobCallBack<BmobIMMessage> bmobCallBack) {
            BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
            //TODO 会话：4.1、创建一个暂态会话入口，发送同意好友请求
            BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
            //TODO 消息：5.1、根据会话入口获取消息管理，发送同意好友请求
            BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
            //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
            AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
            final User currentUser = BmobUser.getCurrentUser(User.class);
            msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//这句话是直接存储到对方的消息表中的
            Map<String, Object> map = new HashMap<>();
            map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
            map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
            map.put("time", add.getTime());//添加好友的请求时间
            msg.setExtraMap(map);
            messageManager.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    bmobCallBack.onResponse(msg, e);
                    if (e == null) {
                        //TODO 3、修改本地的好友请求记录
                        add.setStatus(Config.STATUS_VERIFIED);
                        DBManager.instance().updateNewFriend(add);
                    }
                }
            });
        }
    }

    /////////////////////////////////消息接收/////////////////////////////////
    public static class MassageReceiver {
        private static MassageReceiver instance;

        private MassageReceiver() {
        }

        public static MassageReceiver getInstance() {
            if (instance == null) {
                synchronized (MassageReceiver.class) {
                    instance = new MassageReceiver();
                }
            }
            return instance;
        }
         /*
    3.8.1、自定义消息接收器
    3.8.1.1、自定义消息接收器继承自BmobIMMessageHandler来处理服务器发来的消息和离线消息。


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
    }

    /////////////////////////////////好友管理/////////////////////////////////
    public static class FriendManager {
        private static FriendManager instance;

        private FriendManager() {
        }

        public static FriendManager getInstance() {
            if (instance == null) {
                synchronized (FriendManager.class) {
                    instance = new FriendManager();
                }
            }
            return instance;
        }

        /**
         * 查询好友
         *
         * @param bmobCallBack
         */
        public void queryFriends(final BmobCallBack<List<Friend>> bmobCallBack) {
            BmobQuery<Friend> query = new BmobQuery<>();
            User user = UserApi.getInstance().getUserInfo();
            query.addWhereEqualTo("user", user);
            query.include("friendUser");
            query.order("-updatedAt");
            query.findObjects(new FindListener<Friend>() {
                @Override
                public void done(List<Friend> list, BmobException e) {
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
        public void deleteFriend(Friend f, final BmobCallBack bmobCallBack) {
            Friend friend = new Friend();
            friend.delete(f.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    bmobCallBack.onResponse(null, e);
                }
            });
        }

        /**
         * 添加好友
         *
         * @param friend
         * @param bmobCallBack
         */
        public void addFriend(User friend, final BmobCallBack<String> bmobCallBack) {
            Friend f = new Friend();
            User user = BmobUser.getCurrentUser(User.class);
            f.setUser(user);
            f.setFriendUser(friend);
            f.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobCallBack.onResponse(s, e);
                }
            });
        }

    }

}
