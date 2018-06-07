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
import com.zhangteng.swiperecyclerview.widget.SlideMenuRecyclerViewItem;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.conversation.Conversation;
import com.zhangteng.xim.bmob.conversation.NewFriendConversation;
import com.zhangteng.xim.bmob.conversation.PrivateConversation;
import com.zhangteng.xim.utils.DateUtils;

import java.util.List;

/**
 * Created by swing on 2018/5/18.
 */
public class MessageAdapter extends BaseAdapter<Conversation> {
    private Context context;

    public MessageAdapter(List data, Context context) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(new SlideMenuRecyclerViewItem(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (data.get(position) instanceof PrivateConversation) {
            final PrivateConversation privateConversation = (PrivateConversation) data.get(position);
            View contentView = LayoutInflater.from(context).inflate(R.layout.content_item, ((MyViewHolder) holder).item, false);
            //添加内容布局&菜单布局
            ((TextView) contentView.findViewById(R.id.content_content)).setText(privateConversation.getLastMessageContent());
            ((TextView) contentView.findViewById(R.id.content_title)).setText(privateConversation.getcName());
            ((TextView) contentView.findViewById(R.id.content_time)).setText(DateUtils.getDay(privateConversation.getLastMessageTime()));
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.app_icon)
                    .centerCrop();
            Glide.with(context)
                    .load(privateConversation.getAvatar())
                    .apply(requestOptions)
                    .into((CircleImageView) contentView.findViewById(R.id.content_avatar));
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    privateConversation.onClick(context);
                }
            });
            ((MyViewHolder) holder).item.addContentView(contentView);
            ((MyViewHolder) holder).item.addMenuView(R.layout.menu_item);
            ((MyViewHolder) holder).item.findViewById(R.id.menu_top).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyViewHolder) holder).item.reset();
                    privateConversation.onTopClik(context);
                    data.remove(privateConversation);
                    data.add(0, privateConversation);
                    notifyDataSetChanged();
                }
            });
            ((MyViewHolder) holder).item.findViewById(R.id.menu_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyViewHolder) holder).item.reset();
                    privateConversation.onDeteleClik(context);
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else if (data.get(position) instanceof NewFriendConversation) {
            final NewFriendConversation newFriendConversation = (NewFriendConversation) data.get(position);
            //todo 好友请求布局
            View contentView = LayoutInflater.from(context).inflate(R.layout.content_item, ((MyViewHolder) holder).item, false);
            //添加内容布局&菜单布局
            ((TextView) contentView.findViewById(R.id.content_content)).setText(newFriendConversation.getLastMessageContent());
            ((TextView) contentView.findViewById(R.id.content_title)).setText(newFriendConversation.getcName());
            ((TextView) contentView.findViewById(R.id.content_time)).setText(DateUtils.getDay(newFriendConversation.getLastMessageTime()));
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.app_icon)
                    .centerCrop();
            Glide.with(context)
                    .load(newFriendConversation.getAvatar())
                    .apply(requestOptions)
                    .into((CircleImageView) contentView.findViewById(R.id.content_avatar));
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newFriendConversation.onClick(context);
                }
            });
            ((MyViewHolder) holder).item.addContentView(contentView);
            ((MyViewHolder) holder).item.addMenuView(R.layout.menu_item);
            ((MyViewHolder) holder).item.findViewById(R.id.menu_top).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyViewHolder) holder).item.reset();
                    newFriendConversation.onTopClik(context);
                    data.remove(newFriendConversation);
                    data.add(0, newFriendConversation);
                    notifyDataSetChanged();
                }
            });
            ((MyViewHolder) holder).item.findViewById(R.id.menu_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyViewHolder) holder).item.reset();
                    newFriendConversation.onDeteleClik(context);
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SlideMenuRecyclerViewItem item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (SlideMenuRecyclerViewItem) itemView;
        }
    }

}
