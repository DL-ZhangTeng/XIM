package com.zhangteng.xim.activity;

import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.shapes.Shape;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.zhangteng.xim.R;
import com.zhangteng.xim.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private SlidingPaneLayout dlDrawerLayout;
    private LinearLayout llContent;
    private NavigationView navigationView;

    @Override
    protected int getResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        dlDrawerLayout = (SlidingPaneLayout) findViewById(R.id.dl_drawerlayout);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        dlDrawerLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
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
    }

    @Override
    protected void initData() {

    }
}
