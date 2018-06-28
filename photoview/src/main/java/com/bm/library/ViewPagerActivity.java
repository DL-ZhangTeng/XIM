package com.bm.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuheng on 2015/8/19.
 */
public class ViewPagerActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private List<String> imgs;
    private int position = 0;
    private LinearLayout mLinearLayout;
    private int mNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Intent intent = getIntent();
        if (intent.hasExtra("imgs")) {
            imgs = intent.getStringArrayListExtra("imgs");
        } else {
            imgs = new ArrayList<>();
        }
        if (intent.hasExtra("current")) {
            position = intent.getIntExtra("current", 0);
        }
        mLinearLayout = findViewById(R.id.linear);
        setIndicator();
        mPager = findViewById(R.id.pager);
        mPager.addOnPageChangeListener(this);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgs.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(ViewPagerActivity.this);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(ViewPagerActivity.this)
                        .load(imgs.get(position))
                        .into(view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mNum = position;
        mPager.setCurrentItem(position, false);
        mLinearLayout.getChildAt(position).setEnabled(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in, R.anim.out);
    }

    /**
     * 获取数据
     */
    private void setIndicator() {
        View view;
        for (String src : imgs) {

            //创建底部指示器(小圆点)
            view = new View(this);
            view.setBackgroundResource(R.drawable.background);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            //设置间隔
            if (!src.equals(imgs.get(0))) {
                layoutParams.leftMargin = 10;
            }
            //添加到LinearLayout
            mLinearLayout.addView(view, layoutParams);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mLinearLayout.getChildAt(mNum).setEnabled(false);
        mLinearLayout.getChildAt(position).setEnabled(true);
        mNum = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
