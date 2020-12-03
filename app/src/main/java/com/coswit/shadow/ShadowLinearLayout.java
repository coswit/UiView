package com.coswit.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
    private int shadowColor = Color.TRANSPARENT;
    private int fillColor = Color.parseColor("#FFF5F6F7");

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
//        setLayerType(LAYER_TYPE_SOFTWARE, null);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setAntiAlias(true);
        mPaint.setShadowLayer(18,0,35,0x32939CA5);
//        mPaint.setShadowLayer(25,0,15,Color.RED);


        RectF shadowRect = new RectF(100, 100, getWidth()-100, getHeight()-100);

        Path  path = new Path();
        path.addRoundRect(shadowRect,20,20, Path.Direction.CCW);
        canvas.drawPath(path,mPaint);
    }
}
