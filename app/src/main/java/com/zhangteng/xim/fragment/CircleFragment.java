package com.zhangteng.xim.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangteng.swiperecyclerview.adapter.HeaderOrFooterAdapter;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.CircleAdapter;
import com.zhangteng.xim.adapter.MessageAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.entity.StoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by swing on 2018/5/17.
 */
public class CircleFragment extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CircleAdapter adapter;
    private HeaderOrFooterAdapter headerOrFooterAdapter;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_circle;
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
        List<StoryEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new StoryEntity());
        }
        adapter = new CircleAdapter(getContext(), list);
        headerOrFooterAdapter = new HeaderOrFooterAdapter(adapter) {
            @Override
            public RecyclerView.ViewHolder createHeaderOrFooterViewHolder(ViewGroup parent, Integer viewInt) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewInt, parent, false));
            }

            @Override
            public void onBindHeaderOrFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int viewType) {

            }

            class HeaderViewHolder extends RecyclerView.ViewHolder {

                public HeaderViewHolder(View itemView) {
                    super(itemView);
                }
            }
        };
        headerOrFooterAdapter.addHeaderView(R.layout.circle_header_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(headerOrFooterAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
    }
}
