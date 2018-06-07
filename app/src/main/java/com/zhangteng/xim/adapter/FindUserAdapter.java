package com.zhangteng.xim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;

import java.util.List;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by swing on 2018/6/7.
 */
public class FindUserAdapter extends BaseAdapter<User> {
    private Context context;
    private ItemOnClickListener onClickListener;


    public FindUserAdapter(Context context, List<User> data) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.linkman_item, parent, false);
        return new FindUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(context)
                .load(data.get(position).getIcoPath())
                .apply(requestOptions)
                .into(((FindUserViewHolder) holder).circleImageView);
        ((FindUserViewHolder) holder).textView.setText(data.get(position).getUsername());
        ((FindUserViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setUserId(data.get(position).getObjectId());
                    info.setName(data.get(position).getUsername());
                    info.setAvatar(data.get(position).getIcoPath());
                    IMApi.LoacalUserManager.getInstance().updateUserInfo(info);
                    onClickListener.onClick(view, position);
                }
            }
        });
    }

    public void setOnClickListener(ItemOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface ItemOnClickListener {
        void onClick(View view, int position);
    }

    public static class FindUserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;

        public FindUserViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.linkman_avatar);
            textView = itemView.findViewById(R.id.linkman_name);

        }
    }
}
