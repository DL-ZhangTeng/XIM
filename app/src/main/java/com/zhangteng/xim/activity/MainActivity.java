package com.zhangteng.xim.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SlidingPaneLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.soundcloud.android.crop.Crop;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.imagepicker.utils.FileUtils;
import com.zhangteng.swiperecyclerview.widget.CircleImageView;
import com.zhangteng.xim.MyApplication;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MainAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.entity.Photo;
import com.zhangteng.xim.bmob.http.IMApi;
import com.zhangteng.xim.dagger2.base.DaggerBaseComponent;
import com.zhangteng.xim.dagger2.component.DaggerMainComponent;
import com.zhangteng.xim.dagger2.module.MainModule;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.event.CircleEvent;
import com.zhangteng.xim.event.RefreshEvent;
import com.zhangteng.xim.mvp.presenter.MainPresenter;
import com.zhangteng.xim.mvp.view.MainView;
import com.zhangteng.xim.utils.ActivityHelper;
import com.zhangteng.xim.utils.StringUtils;
import com.zhangteng.xim.widget.DropDownMenu;
import com.zhangteng.xim.widget.NoScrollViewPager;
import com.zhangteng.xim.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

public class MainActivity extends BaseActivity implements MainView,
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        SlidingPaneLayout.PanelSlideListener,
        RadioGroup.OnCheckedChangeListener {
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
    @Inject
    MainPresenter mainPresenter;
    @Inject
    MainAdapter pagerAdapter;

    private static int REQUEST_CODE = 200;

    private Uri bgPath;
    private List<String> path = new ArrayList<>();
    @Inject
    DropDownMenu dropDownMenu;
    private ImagePickerConfig imagePickerConfig;
    private File cameraTempFile;
    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack() {
        @Override
        public void onSuccess(List<String> photoList) {
            super.onSuccess(photoList);
            if (imagePickerConfig.isMultiSelect()) {
                if (photoList != null && photoList.size() > 0) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.addAll(photoList);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("photoList", arrayList);
                    ActivityHelper.jumpToActivityWithBundle(MainActivity.this, ShareActivity.class, bundle, 1);
                }
            } else {
                if (photoList != null && photoList.size() > 0) {
                    Uri sourceUri = FileProvider.getUriForFile(MainActivity.this, imagePickerConfig.getProvider(), new File(photoList.get(0)));
                    cameraTempFile = FileUtils.createTmpFile(MainActivity.this, imagePickerConfig.getFilePath() + File.separator + "crop");
                    bgPath = FileProvider.getUriForFile(MainActivity.this, imagePickerConfig.getProvider(), cameraTempFile);
                    Crop.of(sourceUri, bgPath).withAspect(20, 7).start(MainActivity.this);
                }
            }
        }
    };
    private View headerView;
    private TextView username;
    private CircleImageView avatar;
    private ImageView code;
    private String objectId;


    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .baseComponent(DaggerBaseComponent.create())
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        DBManager.init(MyApplication.getGlobalContext());
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        objectId = null;
        try {
            objectId = mainPresenter.getUser().getObjectId();
        } catch (NullPointerException e) {
            Log.e("MainActivity", "objectId is null");
        }
        if (StringUtils.isNotEmpty(objectId) &&
                IMApi.IMServiceManager.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            mainPresenter.connectService(this);
        }
        initTitleBar();
        //获取头布局文件
        headerView = navigationView.getHeaderView(0);
        code = headerView.findViewById(R.id.iv_code);
        username = headerView.findViewById(R.id.tv_name);
        avatar = headerView.findViewById(R.id.iv_header);
        TextView state = headerView.findViewById(R.id.tv_state);
        state.setText("");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0, false);
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

        navigationView.setItemIconTintList(null);
        headerView.setOnClickListener(this);
        code.setOnClickListener(this);
        avatar.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        slidingPaneLayout.setPanelSlideListener(this);
        dropDownMenu.setScanOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        mainPresenter.queryHeaderViewBg(this);
        mainPresenter.setUsername();
        mainPresenter.setAvatar();
        mainPresenter.setTitleBarLeftIcon();
        mainPresenter.updateVersion(this, getSupportFragmentManager());

    }

    @Override
    protected void initData() {

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
                    mainPresenter.sendAddFriendMessage(this, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    showToast("解析二维码失败");
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            mainPresenter.uploadThemePhoto(this);
        } else if (requestCode == Crop.REQUEST_CROP + 1000) {
            EventBus.getDefault().post(new CircleEvent());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar:
                if (!slidingPaneLayout.isOpen()) {
                    slidingPaneLayout.openPane();
                }
                break;
            case R.id.headerView:
                initImagePicker(false);
                ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(MainActivity.this);
                break;
            case R.id.iv_code:
                ActivityHelper.jumpToActivity(MainActivity.this, MyCodeActivity.class, 1);
                break;
            case R.id.iv_header:
                ActivityHelper.jumpToActivityForParams(MainActivity.this, FriendInfoActivity.class, "objectId", objectId, 1);
                break;
            case R.id.menu_drop_down_scan:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:

                break;
        }
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

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

    private void initImagePicker(boolean isMultiSelect) {
        imagePickerConfig = new ImagePickerConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）,可以实现ImageLoader自定义（内置Glid实现）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口，可以实现IHandlerCallBack自定义
                .provider("com.zhangteng.xim.fileprovider")   // provider默认com.zhangteng.imagepicker.fileprovider
                .pathList(path)                         // 记录已选的图片
                .multiSelect(isMultiSelect)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(isMultiSelect ? 9 : 1)                             // 配置多选时 的多选数量。    默认：9
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

    @Override
    public void setHeaderViewBg(Photo photo) {
        if (photo != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.header_background)
                    .centerCrop();
            Glide.with(MainActivity.this)
                    .load(photo.getPhoto().getUrl())
                    .apply(requestOptions)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            headerView.setBackground(resource);
                        }
                    });
        }
    }

    @Override
    public void setUsername(String username) {
        this.username.setText(username);
    }

    @Override
    public void setAvatar(String iconPath) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .centerCrop();
        Glide.with(this)
                .load(iconPath)
                .apply(requestOptions)
                .into(avatar);
    }

    @Override
    public void setTitleBarLeftIcon(String iconPath) {
        RequestOptions requestOptions1 = new RequestOptions()
                .placeholder(R.mipmap.app_icon)
                .circleCrop();
        Glide.with(this)
                .load(iconPath)
                .apply(requestOptions1)
                .into(titleBar.getLeftBtn());
    }

    @Override
    public File getCameraTempFile() {
        return cameraTempFile;
    }

    @Override
    public Uri getBgPath() {
        return bgPath;
    }

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

    @Override
    public void onPanelSlide(@NonNull View panel, float slideOffset) {
        int width = panel.getWidth();
        llContent.setAlpha(1);
        navigationView.setTranslationX((float) (-0.618 * width * (1 - slideOffset)));
    }

    @Override
    public void onPanelOpened(@NonNull View panel) {

    }

    @Override
    public void onPanelClosed(@NonNull View panel) {

    }

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
                        initImagePicker(true);
                        ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(MainActivity.this);
                    }
                });
                break;
        }
    }

    private void initTitleBar() {
        titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(0)));
        titleBar.setLeftClickListener(this);
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
                        initImagePicker(true);
                        ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(MainActivity.this);
                    }
                });
                break;
        }
    }
}
