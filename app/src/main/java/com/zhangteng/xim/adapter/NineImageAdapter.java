package com.zhangteng.xim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.xim.R;

import java.util.List;

/**
 * Created by swing on 2018/5/22.
 */
public class NineImageAdapter extends BaseAdapter<String> {
    public NineImageAdapter(List data) {
        super(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NineImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nine_image_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    class NineImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public NineImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nine_image_view);
        }
    }
}
