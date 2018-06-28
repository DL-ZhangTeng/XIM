package com.zhangteng.xim.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.zhangteng.swiperecyclerview.adapter.BaseAdapter;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.activity.FriendInfoActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Like;
import com.zhangteng.xim.bmob.entity.Remark;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.DensityUtils;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.widget.RemarkMenu;
import com.zhangteng.xim.widget.UnLineClickableSpan;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/5/22.
 */
public class CircleAdapter extends BaseAdapter<Story> {
    private Activity context;
    private User user = UserApi.getInstance().getUserInfo();
    private RefreshList refreshList;
    private CommentStory commentStory;

    public void setRefreshList(RefreshList refreshList) {
        this.refreshList = refreshList;
    }

    public void setCommentStory(CommentStory commentStory) {
        this.commentStory = commentStory;
    }

    public CircleAdapter(Activity context, List<Story> data) {
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Story story = data.get(position);
        ((ItemViewHolder) holder).expandableTextView.setText(story.getContent());
        ((ItemViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        ((ItemViewHolder) holder).recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 1;
                outRect.bottom = 1;
                outRect.right = 1;
                outRect.left = 1;
            }
        });
        ((ItemViewHolder) holder).recyclerView.setAdapter(new NineImageAdapter(context, story.getIconPaths()));
        if (story.getUser().getIcoPath() == null) {
            story.setUser(User.getUser(DBManager.instance(DBManager.USERNAME).queryUser(story.getUser().getObjectId())));
        }
        ((ItemViewHolder) holder).name.setText(story.getUser().getUsername());
        ((ItemViewHolder) holder).location.setText("");
        ((ItemViewHolder) holder).time.setText(story.getCreatedAt());
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon).centerCrop();
        Glide.with(context)
                .load(story.getUser().getIcoPath())
                .apply(requestOptions)
                .into(((ItemViewHolder) holder).circleImageView);
        ((ItemViewHolder) holder).circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objectId = data.get(position).getUser().getObjectId();
                ActivityHelper.jumpToActivityForParams(context, FriendInfoActivity.class, "objectId", objectId, 1);
            }
        });
        ((ItemViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objectId = data.get(position).getUser().getObjectId();
                ActivityHelper.jumpToActivityForParams(context, FriendInfoActivity.class, "objectId", objectId, 1);
            }
        });
        ((ItemViewHolder) holder).remark.setOnClickListener(new View.OnClickListener() {
            Like like;

            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                final RemarkMenu remarkMenu = new RemarkMenu(context);
                like = new Like();
                like.setUser(user);
                like.setStory(data.get(position));
                remarkMenu.setLikeOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataApi.getInstance().add(like, new BmobCallBack<String>(context, false) {
                            @Override
                            public void onSuccess(@Nullable String bmobObject) {
                                if (bmobObject.equals("delete")) {
                                    for (Like like : data.get(position).getLikes()) {
                                        if (like.getUser().getObjectId().equals(user.getObjectId())) {
                                            data.get(position).getLikes().remove(like);
                                        }
                                    }
                                } else {
                                    if (data.get(position).getLikes() == null) {
                                        data.get(position).setLikes(new ArrayList<Like>());
                                    }
                                    data.get(position).getLikes().add(like);
                                }
                                if (refreshList != null) {
                                    refreshList.onRefreshList(position);
                                }
                                remarkMenu.dismiss();
                            }

                            @Override
                            public void onFailure(BmobException bmobException) {
                                super.onFailure(bmobException);
                                remarkMenu.dismiss();
                            }
                        });
                    }
                });
                remarkMenu.setRemarkOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (commentStory != null) {
                            commentStory.onComment(position, data.get(position));
                        }
                        remarkMenu.dismiss();
                    }
                });
                remarkMenu.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - DensityUtils.dp2px(context, 160), location[1]);
            }
        });
        initLikeAndComment(holder, position);
    }

    private void initLikeAndComment(RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).constraintLayout.setVisibility(View.GONE);
        //初始化点赞
        if (data.get(position).getLikes() != null && !data.get(position).getLikes().isEmpty()) {
            StringBuilder stringBuffer = new StringBuilder();
            for (Like like1 : data.get(position).getLikes()) {
                stringBuffer.append(like1.getUser().getUsername()).append(" ");
            }
            ((ItemViewHolder) holder).like.setText(stringBuffer.toString());

            if (StringUtils.isEmpty(stringBuffer.toString())) {
                ((ItemViewHolder) holder).like.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).like.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).constraintLayout.setVisibility(View.VISIBLE);
            }
        } else {
            ((ItemViewHolder) holder).like.setVisibility(View.GONE);
        }
        //初始化评论
        if (data.get(position).getRemarks() != null && !data.get(position).getRemarks().isEmpty()) {
            SpannableStringBuilder stringBuffer1 = new SpannableStringBuilder();
            for (final Remark remark1 : data.get(position).getRemarks()) {
                int start = stringBuffer1.length();
                if (remark1.getRemark() != null) {
                    stringBuffer1.append(remark1.getUser().getUsername());
                    if (remark1.getRemark().getUser().getObjectId() != null) {
                        LocalUser localUser = DBManager.instance(DBManager.USERNAME).queryUser(remark1.getRemark().getUser().getObjectId());
                        remark1.getRemark().setUser(User.getUser(localUser));
                    }
                    stringBuffer1.append(" 回复 ").append(remark1.getRemark().getUser().getUsername());
                    stringBuffer1.append(" : ").append(remark1.getContent()).append("\n");
                } else {
                    stringBuffer1.append(remark1.getUser().getUsername());
                    stringBuffer1.append(" : ").append(remark1.getContent()).append("\n");
                }
                UnLineClickableSpan clickableSpan = new UnLineClickableSpan(context) {
                    @Override
                    public void onClick(View view) {
                        if (commentStory != null) {
                            commentStory.onComment(position, data.get(position), remark1);
                        }
                    }
                };
                int end = stringBuffer1.length();
                stringBuffer1.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            ((ItemViewHolder) holder).comment.setMovementMethod(LinkMovementMethod.getInstance());
            ((ItemViewHolder) holder).comment.setText(stringBuffer1);
            ((ItemViewHolder) holder).comment.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
            if (StringUtils.isEmpty(stringBuffer1.toString())) {
                ((ItemViewHolder) holder).comment.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).comment.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).constraintLayout.setVisibility(View.VISIBLE);
            }
        } else {
            ((ItemViewHolder) holder).comment.setVisibility(View.GONE);
        }
    }

    public interface RefreshList {
        void onRefreshList(int position);
    }

    public interface CommentStory {
        void onComment(int position, Story story);

        void onComment(int position, Story story, Remark remark);
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
        ConstraintLayout constraintLayout;
        ImageButton remark;

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
            constraintLayout = itemView.findViewById(R.id.circle_body_bg);
            remark = itemView.findViewById(R.id.circle_body_remark);
        }
    }
}
