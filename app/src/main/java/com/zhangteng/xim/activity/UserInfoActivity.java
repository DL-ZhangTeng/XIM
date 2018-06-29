package com.zhangteng.xim.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.ViewPagerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.soundcloud.android.crop.Crop;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.UpdateUserParams;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.CityNo;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.event.UserRefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;

public class UserInfoActivity extends BaseActivity {
    private User user;
    private ImagePickerConfig imagePickerConfig;
    private ArrayList<String> path;
    private File cameraTempFile;
    private Uri bgPath;
    @BindView(R.id.userinfo_iv_avatar)
    ImageView avatar;
    @BindView(R.id.userinfo_tv_username)
    TextView username;
    @BindView(R.id.userinfo_tv_objectid)
    TextView objectid;
    @BindView(R.id.userinfo_tv_sex)
    TextView sex;
    @BindView(R.id.userinfo_tv_area)
    TextView area;
    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack() {
        @Override
        public void onSuccess(List<String> photoList) {
            super.onSuccess(photoList);
            if (photoList != null && photoList.size() > 0) {
                Uri sourceUri = FileProvider.getUriForFile(Objects.requireNonNull(UserInfoActivity.this), imagePickerConfig.getProvider(), new File(photoList.get(0)));
                cameraTempFile = FileUtils.createTmpFile(UserInfoActivity.this, imagePickerConfig.getFilePath() + File.separator + "crop");
                bgPath = FileProvider.getUriForFile(UserInfoActivity.this, imagePickerConfig.getProvider(), cameraTempFile);
                Crop.of(sourceUri, bgPath).withAspect(1, 1).start(UserInfoActivity.this, Crop.REQUEST_CROP + 2000);
            }
        }
    };
    private String objectId;

    @Override
    protected int getResourceId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        objectId = getIntent().getStringExtra("objectId");
        user = DBManager.instance(DBManager.USERNAME).queryUser(objectId);
        if (user == null) {
            user = UserApi.getInstance().getUserInfo();
        }
        initShow(user);
    }

    private void initShow(User user) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(UserInfoActivity.this)
                .load(user.getIcoPath())
                .apply(requestOptions)
                .into(avatar);
        username.setText(user.getUsername());
        objectid.setText(user.getObjectId());
        sex.setText(user.getSex() == 0 ? "男" : "女");
        if (user.getProvinceId() != 0 || user.getCityId() != 0 || user.getAreaId() != 0) {
            CityNo province = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getProvinceId()));
            CityNo city = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getCityId()));
            CityNo arean = DBManager.instance(DBManager.CITYNODBNAME).queryCityNo(String.valueOf(user.getAreaId()));
            area.setText(String.format("%s  %s  %s",
                    province != null ? province.getRegion() : "",
                    city != null ? city.getRegion() : "",
                    arean != null ? arean.getRegion() : ""));
        }
    }

    @Override
    public void buttonClick(View view) {
        super.buttonClick(view);
        switch (view.getId()) {
            case R.id.userinfo_avatar:
                if (imagePickerConfig == null) {
                    if (path == null) {
                        path = new ArrayList<>();
                    }
                    imagePickerConfig = new ImagePickerConfig.Builder()
                            .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                            .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                            .provider("com.zhangteng.xim.fileprovider")   // provider默认com.zhangteng.imagepicker.fileprovider
                            .pathList(path)                         // 记录已选的图片
                            .multiSelect(false)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                            .maxSize(1)                             // 配置多选时 的多选数量。    默认：9
                            .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                            .filePath("/imagePicker/ImagePickerPictures")          // 图片存放路径
                            .build();
                }
                ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(UserInfoActivity.this);
                break;
            case R.id.userinfo_iv_avatar:
                Intent intent = new Intent(this, ViewPagerActivity.class);
                ArrayList<String> data = new ArrayList<String>();
                data.add(user.getIcoPath());
                intent.putStringArrayListExtra("imgs", data);
                intent.putExtra("current", 0);
                this.startActivity(intent);
                this.overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.userinfo_username:
            case R.id.userinfo_tv_username:
                ActivityHelper.jumpToActivityForParamsAndResult(this, UpdateUserActivity.class, "username", user.getUsername(), 2001, 1);
                break;
            case R.id.userinfo_objectid:
            case R.id.userinfo_tv_objectid:
                break;
            case R.id.userinfo_sex:
            case R.id.userinfo_tv_sex:
                new AlertDialog.Builder(this).setTitle("性别").setSingleChoiceItems(new String[]{"男", "女"}, user.getSex(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final UpdateUserParams updateUserParams = new UpdateUserParams();
                        updateUserParams.setSex(which);
                        UserApi.getInstance().updateUser(updateUserParams, new BmobCallBack(UserInfoActivity.this, false) {
                            @Override
                            public void onSuccess(@Nullable Object bmobObject) {
                                LocalUser localUser = DBManager.instance(DBManager.USERNAME).queryUser(UserApi.getInstance().getUserInfo().getObjectId());
                                localUser.setSex(updateUserParams.getSex());
                                EventBus.getDefault().post(new UserRefreshEvent(User.getUser(localUser)));
                                user.setSex(localUser.getSex());
                                sex.setText(user.getSex() == 0 ? "男" : "女");
                            }

                            @Override
                            public void onFailure(BmobException bmobException) {
                                super.onFailure(bmobException);
                                UserInfoActivity.this.showToast("修改失败");
                            }
                        });
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.userinfo_area:
            case R.id.userinfo_tv_area:
                ActivityHelper.jumpToActivityResult(this, AreaSelectActivity.class, 2002, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Crop.REQUEST_CROP + 2000:
                BmobCallBack<String> bmobCallBack = new BmobCallBack<String>(this, true) {
                    @Override
                    public void onSuccess(@Nullable String bmobObject) {
                        final UpdateUserParams updateUserParams = new UpdateUserParams();
                        updateUserParams.setIcoPath(bmobObject);

                        UserApi.getInstance().updateUser(updateUserParams, new BmobCallBack(UserInfoActivity.this, false) {
                            @Override
                            public void onSuccess(@Nullable Object bmobObject) {
                                user.setIcoPath(updateUserParams.getIcoPath());
                                EventBus.getDefault().post(new UserRefreshEvent(user));
                            }
                        });
                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.mipmap.app_icon)
                                .centerCrop();
                        Glide.with(UserInfoActivity.this)
                                .load(bgPath)
                                .apply(requestOptions)
                                .into(avatar);
                    }
                };
                bmobCallBack.onStart();
                DataApi.getInstance().uploadFile(cameraTempFile.getAbsolutePath(), bmobCallBack);
                break;
            case 2001:
            case 2002:
                User user = (User) data.getSerializableExtra("user");
                initShow(user);
                break;
            default:
                break;
        }
    }

}
