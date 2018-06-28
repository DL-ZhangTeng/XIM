package com.zhangteng.xim.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import com.soundcloud.android.crop.Crop;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.swiperecyclerview.adapter.HeaderOrFooterAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.activity.CommentActivity;
import com.zhangteng.xim.adapter.CircleAdapter;
import com.zhangteng.xim.base.BaseFragment;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.event.CircleCommentEvent;
import com.zhangteng.xim.event.CircleEvent;
import com.zhangteng.xim.utils.ActivityHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/5/17.
 */
public class CircleFragment extends BaseFragment implements CircleAdapter.RefreshList, CircleAdapter.CommentStory {
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
    private ImagePickerConfig imagePickerConfig;
    private List<String> path;
    private File cameraTempFile;
    private Uri bgPath;
    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack() {
        @Override
        public void onSuccess(List<String> photoList) {
            super.onSuccess(photoList);
            if (photoList != null && photoList.size() > 0) {
                Uri sourceUri = FileProvider.getUriForFile(Objects.requireNonNull(CircleFragment.this.getActivity()), imagePickerConfig.getProvider(), new File(photoList.get(0)));
                cameraTempFile = FileUtils.createTmpFile(CircleFragment.this.getActivity(), imagePickerConfig.getFilePath() + File.separator + "crop");
                bgPath = FileProvider.getUriForFile(CircleFragment.this.getActivity(), imagePickerConfig.getProvider(), cameraTempFile);
                Crop.of(sourceUri, bgPath).withAspect(11, 7).start(CircleFragment.this.getActivity(), Crop.REQUEST_CROP + 1000);
            }
        }
    };

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
        adapter = new CircleAdapter(getActivity(), list);
        adapter.setRefreshList(this);
        adapter.setCommentStory(this);
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
                ((HeaderViewHolder) holder).background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imagePickerConfig == null) {
                            if (path == null) {
                                path = new ArrayList<>();
                            }
                            imagePickerConfig = new ImagePickerConfig.Builder()
                                    .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                                    .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                                    .provider("com.zhangteng.xim.fileprovider")   // provider默认com.zhangteng.imagepicker.fileprovider
                                    .pathList(path)                         // 记录已选的图片
                                    .multiSelect(false)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                                    .maxSize(1)                             // 配置多选时 的多选数量。    默认：9
                                    .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                                    .filePath("/imagePicker/ImagePickerPictures")          // 图片存放路径
                                    .build();
                        }
                        ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(CircleFragment.this.getActivity());
                    }
                });

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
        DataApi.getInstance().queryCirclePhoto(user, new BmobCallBack<Photo>(getContext(), false) {
            @Override
            public void onSuccess(@Nullable Photo bmobObject) {
                if (bmobObject != null) {
                    photo = bmobObject;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(CircleEvent event) {
        onActivityResult();
    }

    @Subscribe
    public void onEventMainThread(CircleCommentEvent event) {
        list.get(event.getPosition()).setRemarks(event.getStory().getRemarks());
        onRefreshList(event.getPosition());
    }

    private void onActivityResult() {
        BmobCallBack<String> bmobCallBack = new BmobCallBack<String>(this.getActivity(), true) {
            @Override
            public void onSuccess(@Nullable String bmobObject) {
                Photo photo = new Photo();
                photo.setUser(user);
                photo.setMark("circle");
                photo.setName(cameraTempFile.getName());
                BmobFile bmobFile = new BmobFile(cameraTempFile.getName(), null, bmobObject);
                photo.setPhoto(bmobFile);
                DataApi.getInstance().add(photo, new BmobCallBack<String>(CircleFragment.this.getActivity(), false) {
                    @Override
                    public void onSuccess(@Nullable String bmobObject) {

                    }
                });
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.mipmap.header_background)
                        .centerCrop();
                Glide.with(CircleFragment.this)
                        .load(bgPath)
                        .apply(requestOptions)
                        .into((ImageView) headerOrFooterAdapter.getHeaderViewByType(HeaderOrFooterAdapter.BASE_ITEM_TYPE_HEADER).findViewById(R.id.circle_header_bg));
            }
        };
        bmobCallBack.onStart();
        DataApi.getInstance().uploadFile(cameraTempFile.getAbsolutePath(), bmobCallBack);
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


    @Override
    public void onRefreshList(int position) {
        if (adapter != null && headerOrFooterAdapter != null) {
            adapter.notifyItemChanged(position);
            headerOrFooterAdapter.notifyHFAdpterItemChanged(position);
        }
    }

    @Override
    public void onComment(int position, Story story) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("story", story);
        bundle.putInt("position", position);
        ActivityHelper.jumpToActivityWithBundle(getActivity(), CommentActivity.class, bundle, 0);
    }

    @Override
    public void onComment(int position, Story story, Remark remark) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("story", story);
        bundle.putSerializable("remark", remark);
        bundle.putInt("position", position);
        ActivityHelper.jumpToActivityWithBundle(getActivity(), CommentActivity.class, bundle, 0);
    }
}
