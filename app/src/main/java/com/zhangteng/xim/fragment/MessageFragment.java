package com.zhangteng.xim.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MessageAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.http.IMApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by swing on 2018/5/17.
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BmobIMConversation> data;
    private MessageAdapter messageAdapter;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(200);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(200);
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
        if (IMApi.ConversationManager.getInstance().loadAllConversation() != null) {
            data.clear();
            data.addAll(IMApi.ConversationManager.getInstance().loadAllConversation());
            messageAdapter.notifyDataSetChanged();
        }
    }
}
