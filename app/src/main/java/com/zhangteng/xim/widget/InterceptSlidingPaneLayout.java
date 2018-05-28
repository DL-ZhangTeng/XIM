package com.zhangteng.xim.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by swing on 2018/5/21.
 */
public class InterceptSlidingPaneLayout extends SlidingPaneLayout {
    public InterceptSlidingPaneLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptSlidingPaneLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptSlidingPaneLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOpen()) {
            return super.onInterceptTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() <= 2) {
            return true;
        } else {
            return false;
        }
    }
}
