package com.zhangteng.xim.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.FindUserAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class FindUserActivity extends BaseActivity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    String objectId;
    List<User> users;
    FindUserAdapter findUserAdapter;

    @Override
    protected int getResourceId() {
        return R.layout.activity_find_user;
    }

    @Override
    protected void initView() {
        users = new ArrayList<>();
        findUserAdapter = new FindUserAdapter(this, users);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                objectId = titleBar.getFoundText();
                UserApi.getInstance().queryUser(objectId, new BmobCallBack<User>(FindUserActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable User bmobObject) {
                        users.clear();
                        users.add(bmobObject);
                        findUserAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(findUserAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        titleBar.setFoundClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectId = titleBar.getFoundText();
                UserApi.getInstance().queryUser(objectId, new BmobCallBack<User>(FindUserActivity.this, false) {
                    @Override
                    public void onSuccess(@Nullable User bmobObject) {
                        users.clear();
                        users.add(bmobObject);
                        findUserAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });

        findUserAdapter.setOnClickListener(new FindUserAdapter.ItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (users.size() > position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", users.get(position));
                    ActivityHelper.jumpToActivityWithBundle(FindUserActivity.this, FriendInfoActivity.class, bundle, 1);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
