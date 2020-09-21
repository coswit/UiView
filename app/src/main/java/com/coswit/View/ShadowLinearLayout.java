package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author Created by zhengjing on 2019-10-24.
 */
public class ShadowLinearLayout extends LinearLayout {

    private Paint mPaint;
    private Paint mLocationPaint;
    //阴影半径
    private float shadowRadius = 10f;
    //模糊度半径
    private float blurRadius = 20f;
    //背景色
    private int bgColor = Color.WHITE;

    //阴影颜色
    private int shadowColor = Color.parseColor("#33333333");

    public ShadowLinearLayout(Context context) {
        this(context, null);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPaint = new Paint();
        mLocationPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mPaint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL));
//        mPaint.setColor(shadowColor);
//        mPaint.setAntiAlias(true);
//
//        RectF shadowRect = new RectF(0, 0, getWidth(), getHeight());
//
//        RectF locationRect = new RectF(10, 10, getWidth()-10, getHeight()-10);
//        mLocationPaint.setColor(bgColor);
//        mLocationPaint.setAntiAlias(true);
//        canvas.drawRoundRect(shadowRect, shadowRadius, shadowRadius, mPaint);
//        canvas.drawRoundRect(locationRect, shadowRadius, shadowRadius, mLocationPaint);
    }
}
