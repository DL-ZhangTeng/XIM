package com.zhangteng.xim.event;

import android.app.Activity;

/**
 * Created by swing on 2018/6/13.
 */
public class JumpEvent {
    private Activity activity;

    public JumpEvent(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
