package com.zhangteng.xim.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.ViewPagerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.xim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/5/22.
 */
public class NineImageAdapter extends BaseAdapter<String> {
    private Activity context;

    public NineImageAdapter(Activity context, List data) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NineImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nine_image_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon).centerCrop();
        Glide.with(context)
                .load(data.get(position))
                .apply(requestOptions)
                .into(((NineImageViewHolder) holder).imageView);
        ((NineImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPagerActivity.class);
                intent.putStringArrayListExtra("imgs", (ArrayList<String>) data);
                intent.putExtra("current", position);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
    }

    class NineImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public NineImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nine_image_view);
        }
    }
}
