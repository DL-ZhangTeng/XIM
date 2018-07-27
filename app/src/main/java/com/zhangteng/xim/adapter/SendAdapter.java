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
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;

import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by swing on 2018/5/30.
 */
public class SendAdapter extends BaseAdapter<BmobIMMessage> {
    private static final int RIGHT = 1;
    private static final int LEFT = 0;
    private static final int LOADING = -1;
    private Context context;

    public SendAdapter(Context context, List<BmobIMMessage> data) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.message_left_item, parent, false);
        } else if (viewType == RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.message_right_item, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(context)
                .load(IMApi.LoacalUserManager.getInstance().getUserInfo(data.get(position).getFromId()).getAvatar())
                .apply(requestOptions)
                .into(((MessageViewHolder) holder).circleImageView);
        ((MessageViewHolder) holder).message.setText(data.get(position).getContent());
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getFromId().equals(UserApi.getInstance().getUserInfo().getObjectId())) {
            if (position > 0
                    && data.get(position).getContent().equals(data.get(position - 1).getContent())
                    && data.get(position).getToId().equals(UserApi.getInstance().getUserInfo().getObjectId())) {
                return LEFT;
            }
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    public void addMessage(BmobIMMessage message) {
        data.add(message);
        notifyDataSetChanged();
    }

    public void deleteMessage(BmobIMMessage message) {
        data.remove(message);
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView message;

        public MessageViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.message_avatar);
            message = itemView.findViewById(R.id.message);
        }
    }

}
