package com.zhangteng.xim.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhangteng.swiperecyclerview.bean.GroupInfo;
import com.zhangteng.swiperecyclerview.widget.ItemStickyDecoration;
import com.zhangteng.xim.R;
import com.zhangteng.xim.activity.FriendInfoActivity;
import com.zhangteng.xim.adapter.LinkmanAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.event.UserRefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.SortUtils;
import com.zhangteng.xim.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/5/17.
 */
public class LinkmanFragment extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LinkmanAdapter linkmanAdapter;
    private List<Friend> list;
    private ItemStickyDecoration.GroupInfoInterface groupInfoInterface;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_linkman;
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                queryData();
            }
        });
        groupInfoInterface = new ItemStickyDecoration.GroupInfoInterface() {
            @Override
            public GroupInfo getGroupInfo(int position) {
                return list.get(position).getGroupInfo();
            }
        };
        list = new ArrayList<>();
        linkmanAdapter = new LinkmanAdapter(list, this.getContext());
        linkmanAdapter.setOnClickListener(new LinkmanAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (list.size() > position) {
                    String objectId = list.get(position).getFriendUser().getObjectId();
                    ActivityHelper.jumpToActivityForParams(LinkmanFragment.this.getActivity(), FriendInfoActivity.class, "objectId", objectId, 1);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(linkmanAdapter);
        ItemStickyDecoration itemStickyDecoration = new ItemStickyDecoration(groupInfoInterface);
        itemStickyDecoration.setmStickyHeight((int) getResources().getDimension(R.dimen.sticky_height));
        itemStickyDecoration.setTextSize((int) getResources().getDimension(R.dimen.sticky_text_size));
        itemStickyDecoration.setTextPadding((int) getResources().getDimension(R.dimen.common_leftorright_padding));
        recyclerView.addItemDecoration(itemStickyDecoration);
    }

    @Override
    protected void initData() {
        super.initData();
        long index = DBManager.instance(DBManager.USERNAME).countUser();
        if (index <= 0) {
            IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
                @Override
                public void onSuccess(@Nullable List<Friend> bmobObject) {
                    sortData(bmobObject);
                }
            });
        } else {
            List<LocalUser> localUsers = DBManager.instance(DBManager.USERNAME).queryUsers(0, Integer.MAX_VALUE);
            list.clear();
            for (LocalUser user : localUsers) {
                Friend friend = new Friend();
                friend.setUser(UserApi.getInstance().getUserInfo());
                friend.setFriendUser(User.getUser(user));
                friend.setGroupInfo(user.getGroupInfo());
                list.add(friend);
            }
            linkmanAdapter.notifyDataSetChanged();
        }
    }

    private void sortData(List<Friend> bmobObject) {
        list.clear();
        if (bmobObject == null) {
            bmobObject = new ArrayList<>();
        }
        for (Friend friend : bmobObject) {
            if (StringUtils.isNotEmpty(friend.getFriendUser().getObjectId())) {
                list.add(friend);
            }
        }
        GroupInfo.initTotals();
        Collections.sort(list);
        linkmanAdapter.notifyDataSetChanged();
        for (Friend friend : list) {
            char other = SortUtils.getFirstC(friend.getFriendUser().getUsername());
            if (friend.getGroupInfo() == null) {
                friend.setGroupInfo(new GroupInfo());
                friend.getGroupInfo().setPosition(GroupInfo.totals[other - 'A']);
                GroupInfo.totals[other - 'A']++;
                friend.getGroupInfo().setTitle(String.valueOf(other));
                friend.getGroupInfo().setGroupNum(other);
            }
            friend.getGroupInfo().setTotal(GroupInfo.totals[friend.getGroupInfo().getGroupNum() - 'A']);
            LocalUser user = LocalUser.getLocalUser(friend);
            DBManager.instance(DBManager.USERNAME).updateUser(user);
        }
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Friend> bmobObject) {
                sortData(bmobObject);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(UserRefreshEvent event) {
        queryData();
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

    private void queryData() {
        IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Friend> bmobObject) {
                sortData(bmobObject);
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                Toast.makeText(LinkmanFragment.this.getContext(), "queryfriends_failure", Toast.LENGTH_SHORT).show();
                refreshLayout.finishRefresh();
            }
        });
    }
}
