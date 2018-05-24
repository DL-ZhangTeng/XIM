package com.zhangteng.xim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.bmob.entity.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swing on 2018/5/22.
 */
public class CircleAdapter extends BaseAdapter<Story> {
    private Context context;

    public CircleAdapter(Context context, List<Story> data) {
        super(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_body_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).expandableTextView.setText("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" +
                "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        ((ItemViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add("");
        }
        ((ItemViewHolder) holder).recyclerView.setAdapter(new NineImageAdapter(list));

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ExpandableTextView expandableTextView;
        CircleImageView circleImageView;
        TextView name;
        RecyclerView recyclerView;
        TextView location;
        TextView time;
        TextView like;
        TextView comment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            expandableTextView = itemView.findViewById(R.id.expand_text_view);
            circleImageView = itemView.findViewById(R.id.circle_body_header);
            name = itemView.findViewById(R.id.circle_body_name);
            recyclerView = itemView.findViewById(R.id.circle_body_recyclerview);
            location = itemView.findViewById(R.id.circle_body_location);
            time = itemView.findViewById(R.id.circle_body_time);
            like = itemView.findViewById(R.id.circle_body_like);
            comment = itemView.findViewById(R.id.circle_body_comment);
        }
    }
}
