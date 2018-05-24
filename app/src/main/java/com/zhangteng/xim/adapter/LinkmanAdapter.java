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
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.entity.Friend;

import java.util.List;

/**
 * Created by swing on 2018/5/21.
 */
public class LinkmanAdapter extends BaseAdapter<Friend> {
    private Context context;

    public LinkmanAdapter(List data, Context context) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.linkman_item, parent, false);
        return new LinkmanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((LinkmanViewHolder) holder).circleImageView.setImageResource(R.mipmap.ic_launcher);
        ((LinkmanViewHolder) holder).textView.setText(String.valueOf(position));
    }

    public static class LinkmanViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;

        public LinkmanViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.linkman_avatar);
            textView = itemView.findViewById(R.id.linkman_name);

        }
    }
}
