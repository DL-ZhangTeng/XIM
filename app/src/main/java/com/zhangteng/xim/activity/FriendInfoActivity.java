package com.zhangteng.xim.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.CityNo;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.AppManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by swing on 2018/5/28.
 */
public class FriendInfoActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    CircleImageView circleImageView;
    @BindView(R.id.tv_name)
    TextView realName;
    @BindView(R.id.iv_sex)
    ImageView sex;
    @BindView(R.id.tv_accout)
    TextView username;
    @BindView(R.id.tv_region)
    TextView area;
    @BindView(R.id.tv_sign)
    TextView sign;
    @BindView(R.id.img_photo1)
    ImageView photo1;
    @BindView(R.id.img_photo2)
    ImageView photo2;
    @BindView(R.id.img_photo3)
    ImageView photo3;
    @BindView(R.id.tv_photo_temp)
    TextView photos;
    @BindView(R.id.btn_sendmsg)
    Button send;
    private String objectId;
    private LocalUser user;

    @Override
    protected int getResourceId() {
        return R.layout.activity_friend_info;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("objectId")) {
            objectId = getIntent().getStringExtra("objectId");
            user = DBManager.instance(DBManager.USERNAME).queryUser(objectId);
            send.setText("发消息");
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityHelper.jumpToActivityForParams(FriendInfoActivity.this, SendActivity.class, "objectId", user.getObjectId(), 1);
                    AppManager.finishActivity(FriendInfoActivity.this);
                }
            });
        } else if (intent.getExtras().containsKey("user")) {
            user = LocalUser.getLocalUser((User) intent.getExtras().getSerializable("user"));
            send.setText("加好友");
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final BmobIMUserInfo userInfo = new BmobIMUserInfo();
                    userInfo.setName(user.getUsername());
                    userInfo.setAvatar(user.getIcoPath());
                    userInfo.setUserId(user.getObjectId());
                    BmobCallBack bmobCallBack = new BmobCallBack<BmobIMMessage>(FriendInfoActivity.this, true) {
                        @Override
                        public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                            EventBus.getDefault().post(new RefreshEvent());
                        }

                        @Override
                        public void onFailure(BmobException bmobException) {
                            super.onFailure(bmobException);
                            Toast.makeText(FriendInfoActivity.this, "发送请求失败", Toast.LENGTH_LONG).show();
                        }
                    };
                    bmobCallBack.onStart();
                    IMApi.MassageSender.getInstance().sendAddFriendMessage(userInfo, bmobCallBack);
                    AppManager.finishActivity(FriendInfoActivity.this);
                }
            });
        }
        realName.setText(user.getUsername());
        sex.setImageResource(user.getSex() == 0 ? R.mipmap.ic_sex_male : R.mipmap.ic_sex_female);
        username.setText(String.format("XIM账号：%s", user.getObjectId()));
        if (user.getProvinceId() != 0 || user.getCityId() != 0 || user.getAreaId() != 0) {
            CityNo province = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getProvinceId()));
            CityNo city = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getCityId()));
            CityNo arean = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getAreaId()));
            area.setText(String.format("%s  %s  %s",
                    province != null ? province.getRegion() : "",
                    city != null ? city.getRegion() : "",
                    arean != null ? arean.getRegion() : ""));
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(this)
                .load(user.getIcoPath())
                .apply(requestOptions)
                .into(circleImageView);
    }
}
