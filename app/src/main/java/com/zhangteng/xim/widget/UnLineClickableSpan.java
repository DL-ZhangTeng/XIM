package com.zhangteng.xim.widget;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.zhangteng.xim.R;

/**
 * Created by swing on 2018/6/27.
 */
public abstract class UnLineClickableSpan extends ClickableSpan {
    private Context context;

    public UnLineClickableSpan(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(ds.linkColor);
        ds.setColor(context.getResources().getColor(R.color.circle_text));
        ds.setUnderlineText(false); //去掉下划线
    }
}
