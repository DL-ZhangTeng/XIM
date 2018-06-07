package com.zhangteng.xim.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MessageAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.conversation.Conversation;
import com.zhangteng.xim.bmob.conversation.NewFriendConversation;
import com.zhangteng.xim.bmob.conversation.PrivateConversation;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.NewFriend;
import com.zhangteng.xim.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * Created by swing on 2018/5/17.
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Conversation> data;
    private MessageAdapter messageAdapter;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                data.clear();
                data.addAll(getConversations());
                messageAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });
        data = new ArrayList<>();
        messageAdapter = new MessageAdapter(data, this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(messageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initData() {
        super.initData();
        data.clear();
        data.addAll(getConversations());
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //因为新增`新朋友`这种会话类型
        messageAdapter.setData(getConversations());
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        //重新刷新列表
        Log.e("event", "test");
        messageAdapter.setData(getConversations());
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 注册消息接收事件
     *
     * @param event 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     *              2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        //重新获取本地消息并刷新列表
        messageAdapter.setData(getConversations());
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     *
     * @return
     */
    private List<Conversation> getConversations() {
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = DBManager.instance(DBManager.USERNAME).getAllNewFriend();
        if (friends != null && friends.size() > 0) {
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //TODO 会话：4.2、查询全部会话
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }

        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
