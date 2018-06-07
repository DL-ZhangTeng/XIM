package com.zhangteng.xim.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangteng.xim.R;


public class TitleBar extends RelativeLayout {

    private final static int DEFAULT_BTN_LENGTH = 12;
    private final static int DEFAULT_TEXT_SIZE = 12;
    private RelativeLayout titlebarLeftBack;

    private Context mContext;
    private ImageView leftBtn;
    private Button leftSmallBtn;
    private TextView title;
    private ImageView rightSmall;
    private Button rightBtn;
    private int leftBtnWidth = DEFAULT_BTN_LENGTH;
    private int leftBtnHeight = DEFAULT_BTN_LENGTH;
    private int rightBtnWidth = DEFAULT_BTN_LENGTH;
    private int rightBtnHeight = DEFAULT_BTN_LENGTH;
    private boolean isShowDrawable = false;
    private FrameLayout foundFl;
    private EditText foundEt;
    private ImageView foundIv;
    private RelativeLayout titleRL;

    public TitleBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        // TODO Auto-generated constructor stub

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyTitleBar);
        LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);
        leftBtn = (ImageView) this.findViewById(R.id.titlebar_left);
        titlebarLeftBack = (RelativeLayout) this.findViewById(R.id.titlebar_left_back);
        titleRL = this.findViewById(R.id.titlebar_title_rl);
        title = (TextView) this.findViewById(R.id.titlebar_title);
        title.setCompoundDrawables(null, null, null, null);
        rightSmall = (ImageView) this.findViewById(R.id.titlebar_right_button);
        rightBtn = (Button) this.findViewById(R.id.titlebar_right);
        foundFl = (FrameLayout) this.findViewById(R.id.fl_titlebar_found);
        foundEt = (EditText) this.findViewById(R.id.titlebar_found);
        foundIv = (ImageView) this.findViewById(R.id.titlebar_found_logo);
//        int len =  mContext.getResources().getDimensionPixelSize(R.dimen.activity_title_image_length);;
        final int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyTitleBar_titleText:
                    // 获取属性值
                    String titleText = a.getString(attr);
                    title.setText(titleText);
                    break;
                case R.styleable.MyTitleBar_leftButtonText:
                    String leftButtonText = a.getString(attr);
                    //leftBtn.setText(leftButtonText);
                    break;
                case R.styleable.MyTitleBar_rightButtonText:
                    String rightButtonText = a.getString(attr);
                    rightBtn.setText(rightButtonText);
                    break;
                case R.styleable.MyTitleBar_leftButtonBg:
                    int leftBg = a.getResourceId(attr, R.drawable.left_back);
                    leftBtn.setBackgroundResource(leftBg);
                    break;
                case R.styleable.MyTitleBar_leftButtonSrc:
                    int leftSrc = a.getResourceId(attr, R.drawable.left_back);
                    leftBtn.setImageResource(leftSrc);
                    break;
                case R.styleable.MyTitleBar_rightButtonBg:
                    //rightImage.setVisibility(View.VISIBLE);
                    int rightBg = a.getResourceId(attr, R.drawable.left_back);
                    rightBtn.setBackgroundResource(rightBg);
                    break;
                case R.styleable.MyTitleBar_rightButtonSrc:
                    //rightImage.setVisibility(View.VISIBLE);
                    int rightSrc = a.getResourceId(attr, R.drawable.right_menu);
                    rightSmall.setImageResource(rightSrc);
                    break;
                case R.styleable.MyTitleBar_leftButtonShow:
                    boolean isleftShow = a.getBoolean(attr, true);
                    if (isleftShow) {
                        leftBtn.setVisibility(VISIBLE);
                    } else {
                        leftBtn.setVisibility(GONE);
                    }
                    break;
                case R.styleable.MyTitleBar_rightButtonShow:
                    boolean isrightShow = a.getBoolean(attr, true);
                    if (isrightShow) {
                        rightBtn.setVisibility(VISIBLE);
                    } else {
                        rightBtn.setVisibility(GONE);
                    }
                    break;
                case R.styleable.MyTitleBar_rightButtonWidth:
                    rightBtnWidth = a.getDimensionPixelSize(attr, DEFAULT_BTN_LENGTH);
                    break;
                case R.styleable.MyTitleBar_rightButtonHeight:
                    rightBtnHeight = a.getDimensionPixelSize(attr, DEFAULT_BTN_LENGTH);
                    break;
                case R.styleable.MyTitleBar_leftButtonWidth:
                    leftBtnWidth = a.getDimensionPixelSize(attr, DEFAULT_BTN_LENGTH);
                    leftBtn.getLayoutParams().width = leftBtnWidth;
                    break;
                case R.styleable.MyTitleBar_leftButtonHeight:
                    leftBtnHeight = a.getDimensionPixelSize(attr, DEFAULT_BTN_LENGTH);
                    leftBtn.getLayoutParams().height = leftBtnHeight;
                    break;

                case R.styleable.MyTitleBar_titledrawableSrc:
                    boolean titledrawable = a.getBoolean(attr, false);
                    isShowDrawable = titledrawable;
                case R.styleable.MyTitleBar_foundBarShow:
                    boolean isFoundShow = a.getBoolean(attr, false);
                    if (isFoundShow) {
                        titleRL.setVisibility(GONE);
                        foundFl.setVisibility(VISIBLE);
                    } else {
                        foundFl.setVisibility(GONE);
                        titleRL.setVisibility(VISIBLE);
                    }
                    break;
                case R.styleable.MyTitleBar_foundHint:
                    String hint = a.getString(attr);
                    foundEt.setHint(hint);
            }
        }
        a.recycle();
        //initView();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public void setRightShow(boolean rightShow) {
        if (rightShow) {
            rightSmall.setVisibility(VISIBLE);
        } else {
            rightSmall.setVisibility(GONE);
        }
    }

    public void setRightIcon(@DrawableRes int rightSrc) {
        rightSmall.setImageResource(rightSrc);
    }

    private void resetParams(View view, int width, int height) {
        LayoutParams lp = new LayoutParams(width, height);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        view.setLayoutParams(lp);
    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }

    public ImageView getLeftBtn() {
        return leftBtn;
    }

    public void setTitleDrawableRight(boolean upOrDown) {
        if (isShowDrawable) {
            Drawable drawableright = null;
//            if(upOrDown) {
//                drawableright = getResources().getDrawable(R.mipmap.logo_up);
//            }else {
//                drawableright = getResources().getDrawable(R.mipmap.logo_down);
//            }
            if (drawableright != null) {
                drawableright.setBounds(0, 0, drawableright.getMinimumWidth(), drawableright.getMinimumHeight());
                title.setCompoundDrawables(null, null, drawableright, null);
            }
        } else {
            title.setCompoundDrawables(null, null, null, null);
        }
    }

    public void setRightText(String rightText) {
        rightBtn.setText(rightText);
    }

    public void setRightClickListener(OnClickListener onClickListener) {
        rightBtn.setOnClickListener(onClickListener);
    }

    public void setLeftClickListener(OnClickListener onClickListener) {
        leftBtn.setOnClickListener(onClickListener);
    }

    public void setTitleClickListener(OnClickListener ontitleClickListener) {
        if (title != null) {
            title.setOnClickListener(ontitleClickListener);
        }
    }

    public void setFoundClickListener(final OnClickListener onClickListener) {
        foundIv.setOnClickListener(onClickListener);
        foundEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClickListener.onClick(v);
                }
                return false;
            }
        });
    }

    public String getFoundText() {
        return foundEt.getText().toString();
    }

    public void setFoundText(String text) {
        foundEt.setText(text);
    }
}
