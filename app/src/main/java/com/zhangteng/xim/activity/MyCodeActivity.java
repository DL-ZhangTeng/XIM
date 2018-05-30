package com.zhangteng.xim.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.UserApi;

import butterknife.BindView;

/**
 * 我的二维码
 * Created by swing on 2018/5/30.
 */
public class MyCodeActivity extends BaseActivity {
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.tvname)
    TextView name;
    @BindView(R.id.tvmsg)
    TextView msg;
    @BindView(R.id.iv_sex)
    ImageView sex;
    @BindView(R.id.img_code)
    ImageView code;

    @Override
    protected int getResourceId() {
        return R.layout.activity_mycode;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        User user = UserApi.getInstance().getUserInfo();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .circleCrop();
        Glide.with(this)
                .load(user.getIcoPath())
                .apply(requestOptions)
                .into(head);
        name.setText(user.getUsername());
        msg.setText(String.format("XIM号：%s", user.getObjectId()));
        sex.setImageResource(user.getSex() == 0 ? R.mipmap.ic_sex_male : R.mipmap.ic_sex_female);
        code.setImageResource(R.mipmap.default_image);
    }
}
