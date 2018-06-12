package com.zhangteng.xim.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;

/**
 * 动态分享
 * Created by swing on 2018/5/30.
 */
public class ShareActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.et_usertel)
    EditText editText;
    @BindView(R.id.ll_banner)
    LinearLayout linearLayout;
    @BindView(R.id.btnaddimg)
    Button button;
    private ArrayList<String> photoList;
    private ImagePickerConfig imagePickerConfig;
    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack() {
        @Override
        public void onSuccess(List<String> list) {
            super.onSuccess(list);
            if (list != null && list.size() > 0) {
                photoList.clear();
                photoList.addAll(list);
                linearLayout.removeAllViews();
                for (String path : photoList) {
                    ImageView imageView = new ImageView(ShareActivity.this);
                    imageView.setLayoutParams(button.getLayoutParams());
                    RequestOptions requestOptions1 = new RequestOptions()
                            .placeholder(R.mipmap.app_icon)
                            .centerCrop();
                    Glide.with(ShareActivity.this)
                            .load(path)
                            .apply(requestOptions1)
                            .into(imageView);
                    linearLayout.addView(imageView);
                }
            }
        }
    };

    private BmobCallBack bmobCallBack;
    private User user;

    @Override
    protected int getResourceId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initView() {
        user = UserApi.getInstance().getUserInfo();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("photoList")) {
            photoList = bundle.getStringArrayList("photoList");
        } else {
            photoList = new ArrayList<>();
        }
        for (String path : photoList) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(button.getLayoutParams());
            RequestOptions requestOptions1 = new RequestOptions()
                    .placeholder(R.mipmap.app_icon)
                    .centerCrop();
            Glide.with(this)
                    .load(path)
                    .apply(requestOptions1)
                    .into(imageView);
            linearLayout.addView(imageView);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initImagePicker();
                ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(ShareActivity.this);
            }
        });
        titleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmobCallBack = new BmobCallBack<List<String>>(ShareActivity.this, true, true, "正在上传，请稍等...", true) {
                    @Override
                    public void onSuccess(@Nullable List<String> bmobObject) {
                        Story story = new Story();
                        story.setUser(UserApi.getInstance().getUserInfo());
                        story.setContent(editText.getText().toString());
                        story.setIconPaths(bmobObject);
                        story.setGeoPoint(new BmobGeoPoint());
                        DataApi.getInstance().add(story, new BmobCallBack<String>(ShareActivity.this, false) {
                            @Override
                            public void onSuccess(@Nullable String bmobObject) {
                                AppManager.finishActivity(ShareActivity.this);
                            }

                            @Override
                            public void onFailure(BmobException bmobException) {
                                super.onFailure(bmobException);
                                showToast(bmobException.getMessage());
                            }
                        });
//                        for (String path : bmobObject) {
//                            Photo photo = new Photo();
//                            photo.setName(path);
//                            photo.setMark("photo");
//                            photo.setUser(user);
//                            BmobFile bmobFile = new BmobFile(path, null, path);
//                            photo.setPhoto(bmobFile);
//                            DataApi.getInstance().add(photo, new BmobCallBack<String>(ShareActivity.this, false) {
//                                @Override
//                                public void onSuccess(@Nullable String bmobObject) {
//
//                                }
//                            });
//                        }
                    }

                    @Override
                    public void onFailure(BmobException bmobException) {
                        super.onFailure(bmobException);
                        showToast(bmobException.getMessage());
                    }
                };
                bmobCallBack.onStart();
                DataApi.getInstance().uploadBatch(photoList.toArray(new String[photoList.size()]), bmobCallBack);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void initImagePicker() {
        imagePickerConfig = new ImagePickerConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                .provider("com.zhangteng.xim.fileprovider")   // provider默认com.zhangteng.imagepicker.fileprovider
                .pathList(photoList)                         // 记录已选的图片
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/imagePicker/ImagePickerPictures")          // 图片存放路径
                .build();
    }

    @Override
    public void finish() {
        if (bmobCallBack != null)
            bmobCallBack.dismissLongTimeProgressDialog();
        super.finish();
    }
}
