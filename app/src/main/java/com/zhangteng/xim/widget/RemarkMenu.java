package com.zhangteng.xim.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhangteng.xim.R;

/**
 * Created by swing on 2018/6/13.
 */
public class RemarkMenu extends PopupWindow {
    private Context context;
    private TextView like;
    private TextView comment;

    public RemarkMenu(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_remark, null, false);
        comment = view.findViewById(R.id.menu_comment);
        like = view.findViewById(R.id.menu_like);
        this.setContentView(view);
        //设置高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置宽
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //设置非PopupWindow区域是否可触摸
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.showAsDropLeft);
        //防止被虚拟导航栏阻挡
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setLikeOnClickListener(View.OnClickListener onClickListener) {
        like.setOnClickListener(onClickListener);
    }

    public void setRemarkOnClickListener(View.OnClickListener onClickListener) {
        comment.setOnClickListener(onClickListener);
    }

}
