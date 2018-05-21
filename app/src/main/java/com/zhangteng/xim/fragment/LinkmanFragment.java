package com.zhangteng.xim.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhangteng.swiperecyclerview.bean.GroupInfo;
import com.zhangteng.swiperecyclerview.widget.ItemStickyDecoration;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.LinkmanAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.entity.FriendEntity;

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
        List<FriendEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new FriendEntity());
        }
        linkmanAdapter = new LinkmanAdapter(list, this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(linkmanAdapter);
        ItemStickyDecoration itemStickyDecoration = new ItemStickyDecoration(groupInfoInterface);
        itemStickyDecoration.setmStickyHeight((int) getResources().getDimension(R.dimen.sticky_height));
        itemStickyDecoration.setTextSize((int) getResources().getDimension(R.dimen.sticky_text_size));
        recyclerView.addItemDecoration(itemStickyDecoration);
    }
}
