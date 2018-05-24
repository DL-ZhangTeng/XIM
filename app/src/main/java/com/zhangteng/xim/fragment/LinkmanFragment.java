package com.zhangteng.xim.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zhangteng.swiperecyclerview.bean.GroupInfo;
import com.zhangteng.swiperecyclerview.widget.ItemStickyDecoration;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.LinkmanAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.http.IMApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by swing on 2018/5/17.
 */
public class LinkmanFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LinkmanAdapter linkmanAdapter;
    private List<Friend> list;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_linkman;
    }

    @Override
    protected void initView(View view) {
        ItemStickyDecoration.GroupInfoInterface groupInfoInterface = new ItemStickyDecoration.GroupInfoInterface() {
            @Override
            public GroupInfo getGroupInfo(int position) {
                int groupId = position / 5;
                int index = position % 5;
                GroupInfo groupInfo = new GroupInfo(groupId, String.valueOf(groupId), index, 5);
                groupInfo.setPosition(index);
                return groupInfo;
            }
        };
        list = new ArrayList<>();
        linkmanAdapter = new LinkmanAdapter(list, this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(linkmanAdapter);
        ItemStickyDecoration itemStickyDecoration = new ItemStickyDecoration(groupInfoInterface);
        itemStickyDecoration.setmStickyHeight((int) getResources().getDimension(R.dimen.sticky_height));
        itemStickyDecoration.setTextSize((int) getResources().getDimension(R.dimen.sticky_text_size));
        recyclerView.addItemDecoration(itemStickyDecoration);
    }

    @Override
    protected void initData() {
        super.initData();
        IMApi.FriendManager.getInstance().queryFriends(new BmobCallBack<List<Friend>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Friend> bmobObject) {
                Toast.makeText(LinkmanFragment.this.getContext(), "queryfriends_success", Toast.LENGTH_SHORT).show();
                list.clear();
                list.addAll(bmobObject);
                linkmanAdapter.notifyDataSetChanged();
            }
        });
    }
}
