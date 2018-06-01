package com.zhangteng.swiperecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.zhangteng.loadingview.LoadingView;
import com.zhangteng.swiperecyclerview.R;
import com.zhangteng.swiperecyclerview.adapter.HeaderOrFooterAdapter;

/**
 * Created by swing on 2018/6/1.
 */
public class PullDownLoadingRecyclerView extends RecyclerView {
    /**
     * 阻尼效果
     */
    private final static float OFFSET_RADIO = 3f;
    //    private float mLastY;
    private Context context;
    private float mLastY;
    private HeaderOrFooterAdapter headerOrFooterAdapter;
    private LoadingView header = null;
    private PullDownListener listener;
    private boolean isLoading = false;

    public PullDownLoadingRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public PullDownLoadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PullDownLoadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下的时候记录值
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = e.getRawY();
                //手指滑动的差值
                float distanceY = moveY - mLastY;
                mLastY = moveY;
                //第一个条目完全显示   //头部高度大于0   deltaY大于0  向下移动
                if ((((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0
                        || ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 1)
                        && distanceY > 0) {
                    // 更新头部高度
                    updateHeaderHeight((int) (distanceY / OFFSET_RADIO));
                }
                break;
            case MotionEvent.ACTION_UP:
                if ((((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0
                        || ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 1)
                        && header.getHeight() >= getResources().getDimensionPixelSize(R.dimen.loading_height)) {
                    header.startAnimation();
                    if (listener != null && !isLoading) {
                        isLoading = true;
                        listener.onLoading();
                    }
                }

                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof HeaderOrFooterAdapter)
            this.headerOrFooterAdapter = (HeaderOrFooterAdapter) adapter;
    }

    /**
     * 更新头部刷新
     *
     * @param distance
     */
    private void updateHeaderHeight(int distance) {

        // 设置头部高度,原先的高度加上
        //单头部

        if (headerOrFooterAdapter != null && header == null) {
            header = (LoadingView) headerOrFooterAdapter.getHeaderViewByType(HeaderOrFooterAdapter.BASE_ITEM_TYPE_HEADER);
        }
        if (header != null) {
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
            if ((layoutParams.height + distance) < getResources().getDimensionPixelSize(R.dimen.loading_height)) {
                layoutParams.height = layoutParams.height + distance;
                header.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.loading_height);
                header.setLayoutParams(layoutParams);
            }
        }
        //移动到顶部
        smoothScrollBy(0, 0);
    }

    public void setListener(PullDownListener listener) {
        this.listener = listener;
    }

    public <T> void stopPullDown(final T data) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // 设置头部高度,原先的高度加上
                //单头部
                LoadingView header = null;
                if (headerOrFooterAdapter != null) {
                    header = (LoadingView) headerOrFooterAdapter.getHeaderViewByType(HeaderOrFooterAdapter.BASE_ITEM_TYPE_HEADER);
                }
                if (header != null) {
                    ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
                    layoutParams.height = 0;
                    header.setLayoutParams(layoutParams);
                    header.stopAnimation();
                    if (listener != null && isLoading) {
                        isLoading = false;
                        listener.onFinish(data);
                    }
                }
            }
        }, 500);
    }

    public interface PullDownListener<T> {
        void onLoading();

        void onFinish(T data);
    }
}
