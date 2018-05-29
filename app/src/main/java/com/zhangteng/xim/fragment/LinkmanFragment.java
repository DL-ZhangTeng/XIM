package com.zhangteng.xim.fragment;

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
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.SortUtils;
import com.zhangteng.xim.utils.StringUtils;

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
    private int groupNum = 0;
    private int groupPosition = 0;
    private int groupTotal;
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
                IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
                    @Override
                    public void onSuccess(@Nullable List<Friend> bmobObject) {
                        Toast.makeText(LinkmanFragment.this.getContext(), "queryfriends_success", Toast.LENGTH_SHORT).show();
                        list.clear();
                        for (Friend friend : bmobObject) {
                            if (StringUtils.isNotEmpty(friend.getFriendUser().getObjectId())) {
                                list.add(friend);
                            }
                        }
                        Collections.sort(list);
                        linkmanAdapter.notifyDataSetChanged();
                        for (Friend friend : list) {
                            LocalUser user = LocalUser.getLocalUser(friend.getFriendUser());
                            DBManager.instance().updateUser(user);
                        }
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
        });
        groupInfoInterface = new ItemStickyDecoration.GroupInfoInterface() {
            @Override
            public GroupInfo getGroupInfo(int position) {
                char groupId = SortUtils.getFirstLetterC(list.get(position).getFriendUser().getUsername());
                if (groupId != groupNum) {
                    if (groupNum != 0) {
                        groupTotal = groupPosition + 1;
                    } else {
                        groupTotal = list.size();
                    }
                    groupNum = groupId;
                    groupPosition = 0;

                } else {
                    groupPosition++;
                }
                int index = groupPosition;
                GroupInfo groupInfo = new GroupInfo(groupId, String.valueOf(groupId), index, groupTotal);
                groupInfo.setPosition(index);
                return groupInfo;
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
        long index = DBManager.instance().countUser();
        if (index <= 0) {
            IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
                @Override
                public void onSuccess(@Nullable List<Friend> bmobObject) {
                    Toast.makeText(LinkmanFragment.this.getContext(), "queryfriends_success", Toast.LENGTH_SHORT).show();
                    list.clear();
                    for (Friend friend : bmobObject) {
                        if (StringUtils.isNotEmpty(friend.getFriendUser().getObjectId())) {
                            list.add(friend);
                        }
                    }
                    Collections.sort(list);
                    linkmanAdapter.notifyDataSetChanged();
                    for (Friend friend : list) {
                        LocalUser user = LocalUser.getLocalUser(friend.getFriendUser());
                        DBManager.instance().insertUser(user);
                    }
                }
            });
        } else {
            List<LocalUser> localUsers = DBManager.instance().queryUsers(0, Integer.MAX_VALUE);
            list.clear();
            for (LocalUser user : localUsers) {
                Friend friend = new Friend();
                friend.setUser(UserApi.getInstance().getUserInfo());
                friend.setFriendUser(User.getUser(user));
                list.add(friend);
            }
            linkmanAdapter.notifyDataSetChanged();
        }
    }
}
