package com.zhangteng.xim.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.zhangteng.xim.mvp.base.BaseView;

/**
 * Created by swing on 2018/6/25.
 */
public interface AboutXimView extends BaseView {
    Context getViewContext();

    FragmentManager getViewFragmentManger();

}
