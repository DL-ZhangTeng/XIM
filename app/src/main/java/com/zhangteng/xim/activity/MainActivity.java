package com.zhangteng.xim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MainAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.User;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.widget.DropDownMenu;
import com.zhangteng.xim.widget.NoScrollViewPager;
import com.zhangteng.xim.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends BaseActivity {
    @BindView(R.id.dl_drawerlayout)
    SlidingPaneLayout slidingPaneLayout;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.main_viewpager)
    NoScrollViewPager viewPager;
    @BindView(R.id.main_tab_rg)
    RadioGroup radioGroup;
    @BindView(R.id.title_bar)
    TitleBar titleBar;

    MainAdapter pagerAdapter;

    private static int REQUEST_CODE = 200;

    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    private ImagePickerConfig imagePickerConfig;


    @Override
    protected void initData() {
        User user = UserApi.getInstance().getUserInfo();
        //获取头布局文件
        View headerView = navigationView.getHeaderView(0);
        ImageView code = headerView.findViewById(R.id.iv_code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityHelper.jumpToActivity(MainActivity.this, MyCodeActivity.class, 1);
            }
        });
        TextView name = (TextView) headerView.findViewById(R.id.tv_name);
        name.setText(user.getUsername());
        CircleImageView imageView = headerView.findViewById(R.id.iv_header);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(this)
                .load(user.getIcoPath())
                .apply(requestOptions)
                .into(imageView);
        TextView state = (TextView) headerView.findViewById(R.id.tv_state);
        state.setText("");

        RequestOptions requestOptions1 = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .circleCrop();
        Glide.with(this)
                .load(user.getIcoPath())
                .apply(requestOptions1)
                .into(titleBar.getLeftBtn());
    }

    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack() {
        @Override
        public void onSuccess(List<String> photoList) {
            super.onSuccess(photoList);
            if (photoList != null && photoList.size() > 0) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(photoList);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("photoList", arrayList);
                ActivityHelper.jumpToActivityWithBundle(MainActivity.this, ShareActivity.class, bundle, 1);
            }
        }
    };
    private List<String> path = new ArrayList<>();
    private DropDownMenu dropDownMenu;

    @Override
    protected void initView() {
        DBManager.init(MyApplication.getGlobalContext());
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        String objectId = null;
        try {
            objectId = UserApi.getInstance().getUserInfo().getObjectId();
        } catch (NullPointerException e) {
            Log.e("MainActivity", "objectId is null");
        }
        if (StringUtils.isNotEmpty(objectId) &&
                IMApi.IMServiceManager.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            IMApi.IMServiceManager.getInstance().connectService(new BmobCallBack<String>(MainActivity.this, false) {
                @Override
                public void onSuccess(@Nullable String bmobObject) {
                    IMApi.LoacalUserManager.getInstance()
                            .updateUserInfo(
                                    UserApi.getInstance().getUserInfo().getObjectId()
                                    , TextUtils.isEmpty(
                                            UserApi.getInstance().getUserInfo().getRealName())
                                            ? UserApi.getInstance().getUserInfo().getUsername()
                                            : UserApi.getInstance().getUserInfo().getRealName()
                                    , UserApi.getInstance().getUserInfo().getIcoPath()
                            );
                    EventBus.getDefault().post(new RefreshEvent());
                }

                @Override
                public void onFailure(BmobException bmobException) {
                    super.onFailure(bmobException);
                    Toast.makeText(MainActivity.this, bmobException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        initImagePicker();
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_collection:
                        break;
                    case R.id.navigation_item_album:
                        break;
                    case R.id.navigation_item_file:
                        break;
                    case R.id.navigation_item_setting:
                        ActivityHelper.jumpToActivity(MainActivity.this, SettingActivity.class, 1);
                        break;
                }
                return true;
            }
        });
        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                int width = panel.getWidth();
                llContent.setAlpha(1);
                navigationView.setTranslationX((float) (-0.618 * width * (1 - slideOffset)));
            }

            @Override
            public void onPanelOpened(View panel) {

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });
        pagerAdapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0, false);
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(0)));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!slidingPaneLayout.isOpen()) {
                    slidingPaneLayout.openPane();
                }
            }
        });
        dropDownMenu = new DropDownMenu(MainActivity.this);
        switch (viewPager.getCurrentItem()) {
            case 0:
                titleBar.setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dropDownMenu.showAsDropDown(titleBar, titleBar.getWidth(), 1);
                    }
                });
                break;
            case 1:
                titleBar.setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityHelper.jumpToActivity(MainActivity.this, FindUserActivity.class, 1);
                    }
                });
                break;
            case 2:
                titleBar.setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(MainActivity.this);
                    }
                });
                break;
        }
        dropDownMenu.setScanOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch ((i - 1) % 3) {
                    case 0:
                        viewPager.setCurrentItem(0, false);
                        titleBar.setRightText("");
                        titleBar.setRightIcon(R.mipmap.add);
                        titleBar.setRightShow(true);
                        titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(0)));
                        titleBar.setRightClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (dropDownMenu == null)
                                    dropDownMenu = new DropDownMenu(MainActivity.this);
                                dropDownMenu.showAsDropDown(titleBar, titleBar.getWidth(), 1);
                            }
                        });
                        break;
                    case 1:
                        titleBar.setRightText("添加");
                        titleBar.setRightShow(false);
                        viewPager.setCurrentItem(1, false);
                        titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(1)));
                        titleBar.setRightClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityHelper.jumpToActivity(MainActivity.this, FindUserActivity.class, 1);
                            }
                        });
                        break;
                    case 2:
                        titleBar.setRightText("");
                        titleBar.setRightIcon(R.mipmap.takept);
                        titleBar.setRightShow(true);
                        viewPager.setCurrentItem(2, false);
                        titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(2)));
                        titleBar.setRightClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(MainActivity.this);
                            }
                        });
                        break;
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    final BmobIMUserInfo userInfo = new BmobIMUserInfo();
                    UserApi.getInstance().queryUser(result, new BmobCallBack<User>(this, false) {
                        @Override
                        public void onSuccess(@Nullable User bmobObject) {
                            userInfo.setName(bmobObject.getUsername());
                            userInfo.setAvatar(bmobObject.getIcoPath());
                            userInfo.setUserId(bmobObject.getObjectId());
                            IMApi.MassageSender.getInstance().sendAddFriendMessage(userInfo, new BmobCallBack<BmobIMMessage>(MainActivity.this, false) {
                                @Override
                                public void onSuccess(@Nullable BmobIMMessage bmobObject) {
                                    EventBus.getDefault().post(new RefreshEvent());
                                }

                                @Override
                                public void onFailure(BmobException bmobException) {
                                    super.onFailure(bmobException);
                                    Toast.makeText(MainActivity.this, "发送请求失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(BmobException bmobException) {
                            super.onFailure(bmobException);
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO
        long count = IMApi.ConversationManager.getInstance().getAllUnReadCount();
        if (count > 0) {

        } else {

        }
        //TODO 显示底部导航红点
        if (DBManager.instance(DBManager.USERNAME).hasNewFriendInvitation()) {

        } else {

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initImagePicker() {
        imagePickerConfig = new ImagePickerConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                .provider("com.zhangteng.xim.fileprovider")   // provider默认com.zhangteng.imagepicker.fileprovider
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/imagePicker/ImagePickerPictures")          // 图片存放路径
                .build();
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (slidingPaneLayout.isOpen()) {
            slidingPaneLayout.closePane();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
