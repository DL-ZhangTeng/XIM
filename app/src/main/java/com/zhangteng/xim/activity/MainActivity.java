package com.zhangteng.xim.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhangteng.xim.R;
import com.zhangteng.xim.adapter.MainAdapter;
import com.zhangteng.xim.base.BaseActivity;
import com.zhangteng.xim.widget.NoScrollViewPager;
import com.zhangteng.xim.widget.TitleBar;

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

    @Override
    protected void initView() {
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
                        titleBar.setRightShow(true);
                        break;
                    case 1:
                        titleBar.setRightText("添加");
                        titleBar.setRightShow(false);
                        break;
                    case 2:
                        titleBar.setRightText("更多");
                        titleBar.setRightShow(false);
                        break;
                }

            }
        });
    }


    @Override
    protected void initData() {

    }
}
