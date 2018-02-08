package com.zhangteng.xim.base;

import android.view.View;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhangteng.xim.R;

/**
 * Created by swing on 2017/11/30.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        //解决在三星等手机布局不适配bug
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        int bgColor = getActivity().getApplicationContext().getResources().getColor(R.color.dialog_nav_bg);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(bgColor));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), getResourceId(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    protected abstract int getResourceId();

    protected abstract void initView(View view);
}
