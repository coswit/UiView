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
public class ShadowLinearLayout  extends LinearLayout {

    private Paint mPaint;

    public ShadowLinearLayout(Context context) {
        this(context,null);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setWillNotDraw(false);
        mPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE,null);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Color.BLACK 0xFF000000
        mPaint.setShadowLayer(40,10,10,0x33666666);
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(10,10,getWidth()-10,getHeight()-10,mPaint);
    }
}
