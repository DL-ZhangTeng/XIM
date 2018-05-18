package com.zhangteng.swiperecyclerview.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.zhangteng.swiperecyclerview.R;

/**
 * 侧滑菜单item
 * Created by swing on 2018/5/4.
 */
public class SlideMenuRecyclerViewItem extends HorizontalScrollView {
    private int buttonWidth;
    private boolean isLeft = true;
    private LinearLayout contentLayout;
    private LinearLayout menuLayout;

    public SlideMenuRecyclerViewItem(Context context) {
        super(context);
        init();
    }

    public SlideMenuRecyclerViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenuRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        buttonWidth = dp2px(200);
        setHorizontalScrollBarEnabled(false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        findView(LayoutInflater.from(getContext()).inflate(R.layout.slide_menu_recyclerview_item, this, true));
    }

    private void findView(View view) {
        contentLayout = (LinearLayout) view.findViewById(R.id.slide_menu_rv_content_layout);
        menuLayout = (LinearLayout) view.findViewById(R.id.slide_menu_rv_menu_layout);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //调整布局
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        LinearLayout linearLayoutLeft = (LinearLayout) linearLayout.getChildAt(0);
        LinearLayout linearLayoutRight = (LinearLayout) linearLayout.getChildAt(1);
        linearLayout.layout(linearLayout.getLeft(), linearLayout.getTop(), linearLayout.getLeft() + getMeasuredWidth() + buttonWidth, linearLayout.getBottom());
        linearLayoutLeft.layout(linearLayoutLeft.getLeft(), linearLayoutLeft.getTop(), linearLayoutLeft.getLeft() + getMeasuredWidth(), linearLayoutLeft.getBottom());
        linearLayoutRight.layout(linearLayoutLeft.getLeft() + getMeasuredWidth(), linearLayoutLeft.getTop(), linearLayoutLeft.getLeft() + getMeasuredWidth() + buttonWidth, linearLayoutLeft.getBottom());

    }


    //恢复状态
    public void reset() {
        isLeft = true;
        scrollTo(0, 0);
    }

    public void addContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }

    public void addMenuView(View view) {
        menuLayout.removeAllViews();
        menuLayout.addView(view);
    }

    public void addContentView(@LayoutRes int view) {
        contentLayout.removeAllViews();
        contentLayout.addView(LayoutInflater.from(contentLayout.getContext()).inflate(view, this, false));
    }

    public void addMenuView(@LayoutRes int view) {
        menuLayout.removeAllViews();
        menuLayout.addView(LayoutInflater.from(menuLayout.getContext()).inflate(view, this, false));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {

            int range = 70;
            if (isLeft) {
                if (getScrollX() > range) {
                    isLeft = false;
                    smoothScrollTo(buttonWidth, 0);
                } else {
                    smoothScrollTo(0, 0);
                }
            } else {
                if (getScrollX() < (buttonWidth - range)) {
                    isLeft = true;
                    smoothScrollTo(0, 0);
                } else {
                    smoothScrollTo(buttonWidth, 0);
                }
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }


    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }
}
