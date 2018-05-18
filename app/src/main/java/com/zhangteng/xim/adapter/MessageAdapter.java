package com.zhangteng.xim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.swiperecyclerview.widget.SlideMenuRecyclerViewItem;
import com.zhangteng.xim.R;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by swing on 2018/5/18.
 */
public class MessageAdapter extends BaseAdapter<BmobIMConversation> {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.content_item, ((MyViewHolder) holder).item, false);
        //添加内容布局&菜单布局
        ((TextView) contentView.findViewById(R.id.content_content)).setText("111");
        ((TextView) contentView.findViewById(R.id.content_title)).setText("111");
        ((TextView) contentView.findViewById(R.id.content_time)).setText("111");
        ((CircleImageView) contentView.findViewById(R.id.content_avatar)).setImageResource(R.mipmap.ic_launcher);
        ((MyViewHolder) holder).item.addContentView(contentView);
        ((MyViewHolder) holder).item.addMenuView(R.layout.menu_item);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SlideMenuRecyclerViewItem item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (SlideMenuRecyclerViewItem) itemView;
        }
    }

}
