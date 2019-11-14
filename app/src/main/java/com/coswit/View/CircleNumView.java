package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class CircleNumView extends View {
    private Paint mPaint;
    private Context mContext;

    private String mText;
    private RectF rect;

    private int mType;

    public CircleNumView(Context context) {
        this(context, null);
    }

    public CircleNumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint();
        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        mPaint.reset();
        mPaint.setAntiAlias(true);

        //圆绘制
        if (mType == Type.empty) {
            mPaint.setColor(0xFFE0E0E0);
            canvas.drawCircle(width / 2, height / 2, dp2px(17), mPaint);
            mPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(width / 2, height / 2, dp2px(16), mPaint);
        } else {
            if (mType == Type.WRONG) {
                mPaint.setColor(0xFFFE4343);
            } else {

                mPaint.setColor(0xFF2CC178);
            }
            canvas.drawCircle(width / 2, height / 2, dp2px(17), mPaint);
        }

        //文字绘制
        boolean isEmpty = mType == Type.empty;
        mPaint.setColor(isEmpty ? 0xFF2A2A2A : 0xFFFFFFFF);
        mPaint.setTextSize(sp2px(17));
        mPaint.setTextAlign(Paint.Align.CENTER);
        int xPos = width / 2;
        //((mPaint.descent() + mPaint.ascent()) / 2) is the distance from the baseline to the center.
        int yPos = (int) ((height / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText(mText, xPos, yPos, mPaint);

        //半圆绘制
        if (mType == Type.middle) {
            mPaint.setColor(0x99FFFFFF);
            rect.left = 0 ;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
            canvas.drawArc(rect, -90, 180, true, mPaint);
        }


    }

    public void setText(@Type int type, String text) {
        mType = type;
        mText = text;
        invalidate();
    }

    private int dp2px(final float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(final float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

        /**
         * 错误
         */
        int WRONG = 1;

        /**
         * 正确
         */
        int right = 2;

        /**
         * 未作答
         */
        int empty = 3;

        /**
         * 部分正确
         */
        int middle = 4;
    }
}
