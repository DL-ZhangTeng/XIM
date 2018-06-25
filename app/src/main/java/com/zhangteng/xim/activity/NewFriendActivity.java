package com.zhangteng.xim.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.config.Config;
import com.zhangteng.xim.bmob.conversation.NewFriendConversation;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.utils.AppManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.exception.BmobException;

public class NewFriendActivity extends BaseActivity {
    @BindView(R.id.add_friend_avatar)
    CircleImageView avatar;
    @BindView(R.id.add_friend_title)
    TextView title;
    @BindView(R.id.add_friend_content)
    TextView content;
    @BindView(R.id.add_friend_agree)
    TextView agree;
    @BindView(R.id.add_friend_refuse)
    TextView refuse;
    NewFriendConversation newFriendConversation;

    BmobCallBack bmobCallBack;

    @Override
    protected int getResourceId() {
        return R.layout.activity_new_friend;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra("conversation")) {
            newFriendConversation = (NewFriendConversation) intent.getSerializableExtra("conversation");
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(this)
                .load(newFriendConversation.getNewFriend().getAvatar())
                .apply(requestOptions)
                .into(avatar);
        title.setText(newFriendConversation.getNewFriend().getName());
        content.setText("请求添加好友");
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User();
                user.setObjectId(newFriendConversation.getNewFriend().getUid());
                bmobCallBack = new BmobCallBack<String>(NewFriendActivity.this, true, true) {
                    @Override
                    public void onSuccess(@Nullable String bmobObject) {
                        IMApi.MassageSender.getInstance().sendAgreeAddFriendMessage(newFriendConversation.getNewFriend(), new BmobCallBack<BmobIMMessage>(NewFriendActivity.this, true) {
                            @Override
                            public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                                showToast("已同意");
                                newFriendConversation.getNewFriend().setStatus(Config.STATUS_VERIFIED);
                                DBManager.instance(DBManager.USERNAME).updateNewFriend(newFriendConversation.getNewFriend());
                                EventBus.getDefault().post(new RefreshEvent());
                                bmobCallBack.dismissProgressDialog();
                                AppManager.finishActivity(NewFriendActivity.this);
                            }

                            @Override
                            public void onFailure(BmobException bmobException) {
                                super.onFailure(bmobException);
                                bmobCallBack.dismissProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        showToast("请重新尝试。。。");
                        bmobCallBack.dismissProgressDialog();
                    }
                };
                bmobCallBack.onStart();
                IMApi.FriendManager.getInstance().addFriend(user, bmobCallBack);

            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("已拒绝");
                newFriendConversation.getNewFriend().setStatus(Config.STATUS_VERIFY_REFUSE);
                DBManager.instance(DBManager.USERNAME).updateNewFriend(newFriendConversation.getNewFriend());
                EventBus.getDefault().post(new RefreshEvent());
                bmobCallBack.dismissProgressDialog();
                AppManager.finishActivity(NewFriendActivity.this);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void goBack() {
        super.goBack();
        finish();
    }
}
