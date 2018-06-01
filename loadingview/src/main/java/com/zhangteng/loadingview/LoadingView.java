package com.zhangteng.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by swing on 2018/4/27.
 */
public class LoadingView extends View {
    /**
     * 画笔
     */
    private Paint rightRectPaint;
    private Paint centerRectPaint;
    private Paint leftRectPaint;
    /**
     * 矩形
     */
    private RectF rightRectF;
    private RectF centerRectF;
    private RectF leftRectF;
    /**
     * 矩形最大半径，正方形边长一半
     */
    private float radius;
    /**
     * 保存原始半径，正方形边长一半，用于重设初始状态
     */
    private float resetRadius;
    /**
     * 各个矩形初始半径
     */
    private float rectRightRadius;
    private float rectCenterRadius;
    private float rectLeftRadius;
    /**
     * 原点偏移量
     */
    private float translateXY;
    /**
     * 矩形偏移量（平移量相对于变换前的原始坐标系）
     */
    private float translateX;
    /**
     * 动画速度（越小越快，平移一个矩形长度所用的时间ms）
     */
    private long velocity;
    /**
     * 动画速度，用于初始化状态
     */
    private long resetVelocity;
    /**
     * 矩形颜色
     */
    @ColorInt
    private int rectColor;
    /**
     * 矩形颜色，用于初始化状态
     */
    private int resetColor;
    /**
     * 延时任务，动画
     */
    private Runnable anim = new Runnable() {
        @Override
        public void run() {
            //右矩形从零开始在t时间内曾到最大
            rectRightRadius = rectRightRadius + 1f;
            //中间矩形在t时间内由最大变为一半（为了和初始左矩形一样大小，平移后的位置也要和左矩形初始位置相同）（视觉效果为一个连续的动画）
            rectCenterRadius = rectCenterRadius - 0.5f;
            //左矩形在t时间内从一半减为0
            rectLeftRadius = rectLeftRadius - 0.5f;

            //                if (rectCenterRadius <= 0) {
            //                    rectLeftRadius = 0;
            //                }
            //左矩形小于0则使之消失（负数会显示）
            if (rectLeftRadius <= 0) {
                rectLeftRadius = 0;
            }
            //右边矩形增至最大后不在递增（避免超出最大半径值）
            if (rectRightRadius >= radius) {
                rectRightRadius = rectRightRadius - 1f;
            }
            //在动画时间内及左矩形减为0时间内，平移一个矩形对角线，即每平移一个单位的半径平移一个单位对角线
            translateX = translateX + translateXY * 2 / (radius);
            //平移距离等于一个对角线初始化状态（此时视觉效果为中间矩形变为左矩形，初始化后视觉无法感知到状态初始化）
            if (translateX >= translateXY * 2) {
                reset();
            }
            //每次平移初始化各个矩形
            initRect();
            //重新绘制
            postInvalidate();
            //重新发送定时任务（模拟动画）使用stopAnimation停止
            postDelayed(anim, velocity);
        }
    };
    private boolean isStart = false;
    private float startWidth;
    private float startHeight;

    private boolean selfAdaptation = false;

    public LoadingView(Context context) {
        super(context);
        init(context, null);
        iniPaint();
        initRect();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        iniPaint();
        initRect();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        iniPaint();
        initRect();
    }

    /**
     * 初始化参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        radius = getResources().getDimension(R.dimen.loadingviewdefautradius);
        velocity = getResources().getInteger(R.integer.loadingviewdefautvelocity);
        rectColor = getResources().getColor(R.color.colorAccent);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            int index = a.getIndexCount();
            for (int i = 0; i < index; i++) {
                int i1 = a.getIndex(i);
                if (i1 == R.styleable.LoadingView_loadingviewcolor) {
                    rectColor = a.getColor(i, getResources().getColor(R.color.colorAccent));

                } else if (i1 == R.styleable.LoadingView_loadingviewradius) {
                    radius = a.getDimension(i, getResources().getDimension(R.dimen.loadingviewdefautradius));

                } else if (i1 == R.styleable.LoadingView_loadingviewvelocity) {
                    velocity = (long) (a.getInteger(i, getResources().getInteger(R.integer.loadingviewdefautvelocity)) * 2 / radius);


                } else {
                }
            }
            a.recycle();
        }
        //保存初始状态
        resetRadius = radius;
        resetColor = rectColor;
        resetVelocity = velocity;
        //计算初始数据（各个矩形初始大小，）
        rectRightRadius = 1;
        rectCenterRadius = radius;
        rectLeftRadius = radius / 2;
        //转换坐标需要的平移量（矩形对角线）
        translateXY = (int) Math.sqrt((Math.pow(radius * 2, 2) / 2));
        //默认平移量
        translateX = 0;

    }

    private void iniPaint() {
        rightRectPaint = new Paint();
        rightRectPaint.setAntiAlias(true);
        rightRectPaint.setDither(true);
        rightRectPaint.setStyle(Paint.Style.FILL);
        rightRectPaint.setStrokeWidth(4);
        rightRectPaint.setColor(rectColor);

        centerRectPaint = new Paint();
        centerRectPaint.setDither(true);
        centerRectPaint.setAntiAlias(true);
        centerRectPaint.setStyle(Paint.Style.FILL);
        centerRectPaint.setStrokeWidth(4);
        centerRectPaint.setColor(rectColor);

        leftRectPaint = new Paint();
        leftRectPaint.setDither(true);
        leftRectPaint.setAntiAlias(true);
        leftRectPaint.setStyle(Paint.Style.FILL);
        leftRectPaint.setStrokeWidth(4);
        leftRectPaint.setColor(rectColor);

    }

    private void reset() {
        radius = resetRadius;
        velocity = resetVelocity;
        rectColor = resetColor;
        rectRightRadius = 1;
        rectCenterRadius = radius;
        rectLeftRadius = radius / 2;
        translateXY = (int) Math.sqrt((Math.pow(radius * 2, 2) / 2));
        translateX = 0;
    }

    private void initRect() {
        leftRectF = new RectF();
        //平移量在x，y坐标上的平移距离（如平移根号2cm，则分别在x，y方向平移1cm）
        int t = (int) Math.sqrt(Math.pow(translateX, 2) / 2);
        //重新计算平移后的矩形边界（矩形中心及转换后的坐标原点）
        leftRectF.left = -rectLeftRadius - t;
        leftRectF.top = -rectLeftRadius + t;
        leftRectF.right = rectLeftRadius - t;
        leftRectF.bottom = rectLeftRadius + t;

        centerRectF = new RectF();
        centerRectF.left = -rectCenterRadius - t;
        centerRectF.top = -rectCenterRadius + t;
        centerRectF.right = rectCenterRadius - t;
        centerRectF.bottom = rectCenterRadius + t;

        rightRectF = new RectF();
        rightRectF.left = -rectRightRadius - t;
        rightRectF.top = -rectRightRadius + t;
        rightRectF.right = rectRightRadius - t;
        rightRectF.bottom = rectRightRadius + t;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (selfAdaptation) {
            int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
            int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
            //整个动画占位3个矩形及3个矩形对角线 + 一个矩形对角线（用来左移动画显示） = 8个translateXY
            //当控件宽度小于动画所需宽度，则适应宽度，将radius重设（以specWidthSize / 8为translateXY）
            if (translateXY * 8 > specWidthSize) {
                resetRadius = (float) (Math.sqrt(Math.pow(specWidthSize / 8, 2) * 2) / 2);
                //当高度小于一个对角线则适应高度
                if (translateXY * 2 > specHeightSize) {
                    resetRadius = (float) (Math.sqrt(Math.pow(specHeightSize / 2, 2) * 2) / 2);
                }
                reset();
            } else {
                if (translateXY * 2 > specHeightSize) {
                    resetRadius = (float) (Math.sqrt(Math.pow(specHeightSize / 2, 2) * 2) / 2);
                    reset();
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startWidth = canvas.getWidth() / 2 - translateXY * 4 + translateXY * 2;
        startHeight = canvas.getHeight() / 2 - translateXY;
        //半径大于0则绘制矩形
        if (rectLeftRadius >= 0) {
            //保存初始画布状态（坐标系，及已绘制图画）
            canvas.save();
            //坐标系平移使原点平移到（translateXY，translateXY）
            canvas.translate(startWidth + translateXY, startHeight + translateXY);
            //旋转坐标系45度
            canvas.rotate(45);
            //绘制矩形
            canvas.drawRect(leftRectF, leftRectPaint);
            //回复坐标系到原始状态（已绘制的图像相对设备的位置不变）
            canvas.restore();
        }
        if (rectCenterRadius >= 0) {
            canvas.save();
            //原点在第一个矩形原点基础上平移一个对角线及 translateXY * 3
            canvas.translate(startWidth + translateXY * 3, startHeight + translateXY);
            canvas.rotate(45);
            canvas.drawRect(centerRectF, centerRectPaint);
            canvas.restore();
        }
        if (rectRightRadius >= 0) {
            canvas.save();
            canvas.translate(startWidth + translateXY * 5, startHeight + translateXY);
            canvas.rotate(45);
            canvas.drawRect(rightRectF, rightRectPaint);
            canvas.restore();
        }
    }

    public void startAnimation() {
        if (anim != null && !isStart) {
            //发送定时任务，启动动画
            postDelayed(anim, velocity);
            isStart = true;
        }
    }

    public void stopAnimation() {
        if (anim != null && isStart) {
            removeCallbacks(anim);
            isStart = false;
        }
    }

    public void setSelfAdaptation(boolean selfAdaptation) {

    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.resetRadius = radius;
        reset();
    }

    public long getVelocity() {
        return velocity;
    }

    /**
     * @param velocity 从一个菱形到另一个菱形所用的时间
     */
    public void setVelocity(long velocity) {
        this.resetVelocity = (long) (velocity * 2 / radius);
        reset();
    }

    public int getRectColor() {
        return rectColor;
    }

    public void setRectColor(int rectColor) {
        this.resetColor = rectColor;
        reset();
    }

    public boolean isStart() {
        return isStart;
    }
}
