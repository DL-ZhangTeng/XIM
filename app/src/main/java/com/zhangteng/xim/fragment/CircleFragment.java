package com.zhangteng.xim.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhangteng.swiperecyclerview.adapter.HeaderOrFooterAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.CircleAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/5/17.
 */
public class CircleFragment extends BaseFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private User user;
    private Photo photo;
    private List<Story> list;

    private CircleAdapter adapter;
    private HeaderOrFooterAdapter headerOrFooterAdapter;

    private static int start = 0;
    private static int limit = 100;
    private static int index = 1;

    @Override
    protected int getResourceId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {
        user = UserApi.getInstance().getUserInfo();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                queryStorys(true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                queryStorys(false);
            }
        });
        list = new ArrayList<>();
        adapter = new CircleAdapter(getContext(), list);
        headerOrFooterAdapter = new HeaderOrFooterAdapter(adapter) {
            @Override
            public RecyclerView.ViewHolder createHeaderOrFooterViewHolder(ViewGroup parent, Integer viewInt) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewInt, parent, false));
            }

            @Override
            public void onBindHeaderOrFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int viewType) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.mipmap.app_icon).centerCrop();
                ((HeaderViewHolder) holder).name.setText(user.getUsername());
                Glide.with(Objects.requireNonNull(CircleFragment.this.getContext()))
                        .load(user.getIcoPath())
                        .apply(requestOptions)
                        .into(((HeaderViewHolder) holder).header);
                Glide.with(CircleFragment.this.getContext())
                        .load(photo == null || photo.getPhoto() == null ? "" : photo.getPhoto().getUrl())
                        .apply(requestOptions)
                        .into(((HeaderViewHolder) holder).background);

            }

            class HeaderViewHolder extends RecyclerView.ViewHolder {
                private ImageView background;
                private TextView name;
                private CircleImageView header;

                public HeaderViewHolder(View itemView) {
                    super(itemView);
                    background = itemView.findViewById(R.id.circle_header_bg);
                    name = itemView.findViewById(R.id.circle_header_name);
                    header = itemView.findViewById(R.id.circle_header_head);
                }
            }
        };
        headerOrFooterAdapter.addHeaderView(R.layout.circle_header_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(headerOrFooterAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initData() {
        super.initData();
        photo = new Photo();
        photo.setUser(user);
        DataApi.getInstance().queryPhoto(photo, new BmobCallBack<List<Photo>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Photo> bmobObject) {
                for (Photo photo1 : bmobObject) {
                    if (photo1.getMark().equals("circle")) {
                        photo = photo1;
                    }
                }
                adapter.notifyDataSetChanged();
                headerOrFooterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
            }
        });
        queryStorys(false);
    }

    /**
     * 单人动态查询
     */
    private void queryStory(final boolean isLoad) {
        Story story = new Story();
        if (isLoad && list.size() > 0) {
            story = list.get(list.size() - 1);
        }
        if (story.getUser() == null)
            story.setUser(user);

        DataApi.getInstance().queryStory(story, 10, new BmobCallBack<List<Story>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Story> bmobObject) {
                if (bmobObject != null && !bmobObject.isEmpty()) {
                    if (!isLoad)
                        list.clear();
                    list.addAll(bmobObject);
                    adapter.notifyDataSetChanged();
                    headerOrFooterAdapter.notifyDataSetChanged();
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
    }

    /**
     * 所有动态查询
     */
    private void queryStorys(final boolean isLoad) {
        if (isLoad) {
            start = limit * index;
            index++;
        } else {
            start = 0;
            index = 1;
        }
        DataApi.getInstance().queryStorys(user, start, limit, new BmobCallBack<List<Story>>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable List<Story> bmobObject) {
                if (bmobObject != null && !bmobObject.isEmpty()) {
                    if (!isLoad)
                        list.clear();
                    list.addAll(bmobObject);
                    adapter.notifyDataSetChanged();
                    headerOrFooterAdapter.notifyDataSetChanged();
                }
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(BmobException bmobException) {
                super.onFailure(bmobException);
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
            }
        });
    }
}
