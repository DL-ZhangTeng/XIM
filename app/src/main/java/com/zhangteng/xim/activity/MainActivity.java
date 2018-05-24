package com.zhangteng.xim.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhangteng.imagepicker.callback.HandlerCallBack;
import com.zhangteng.imagepicker.callback.IHandlerCallBack;
import com.zhangteng.imagepicker.config.ImagePickerConfig;
import com.zhangteng.imagepicker.config.ImagePickerOpen;
import com.zhangteng.imagepicker.imageloader.GlideImageLoader;
import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MainAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.bmob.params.LoginParams;
import com.zhangteng.xim.widget.NoScrollViewPager;
import com.zhangteng.xim.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    private ImagePickerConfig imagePickerConfig;


    @Override
    protected void initData() {
        LoginParams loginParams = new LoginParams();
        loginParams.setName("duoluo9");
        loginParams.setPassword("111111");
        UserApi.getInstance().login(loginParams, new BmobCallBack(this, false) {
            @Override
            public void onSuccess(@Nullable Object bmobObject) {
                Toast.makeText(MainActivity.this, "login_success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private IHandlerCallBack iHandlerCallBack = new HandlerCallBack();
    private List<String> path = new ArrayList<>();

    @Override
    protected void initView() {
        initImagePicker();
        navigationView.setItemIconTintList(null);
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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                viewPager.setCurrentItem(i - 1, false);
                titleBar.setTitleText(String.valueOf(pagerAdapter.getPageTitle(i - 1)));
                switch (i - 1) {
                    case 0:
                        titleBar.setRightText("");
                        titleBar.setRightIcon(R.mipmap.add);
                        titleBar.setRightShow(true);
                        break;
                    case 1:
                        titleBar.setRightText("添加");
                        titleBar.setRightShow(false);
                        break;
                    case 2:
                        titleBar.setRightText("");
                        titleBar.setRightIcon(R.mipmap.takept);
                        titleBar.setRightShow(true);
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
