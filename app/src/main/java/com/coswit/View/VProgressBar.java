package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author Created by zhengjing on 2020/7/2.
 */
public class VProgressBar extends View {
    private Context context;
    private int progress = 32;
    private float lineWidth;
    private float radius;

    private Paint mPaint;
    private int mainColor = 0xFF008AFF;
    private int shadowColor = 0xFFC8E4FB;
    private Paint borderPaint;
    private RectF rectf;

    public VProgressBar(Context context) {
        this(context, null);
    }

    public VProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        rectf = new RectF(0, 0, 0, 0);
        lineWidth = dp2px(1);
        radius = dp2px(17);
    }


    public void progress(int progress) {
        this.progress = progress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = getPaddingStart() + getMeasuredWidth() + getPaddingEnd();
        int dh = getPaddingTop() + getMeasuredHeight() + getPaddingBottom();
        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        int w = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        int h = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        float ew = w * (progress / 100f);


        mPaint.reset();
        mPaint.setAntiAlias(true);

        //框线绘制
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(mainColor);
        borderPaint.setDither(true);

        Paint fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setDither(true);
        fillPaint.setColor(0xFFFFFFFF);

        rectf.left = getPaddingStart();
        rectf.right = rectf.left + w;
        rectf.top = getPaddingTop();
        rectf.bottom = rectf.top + h;
        canvas.drawRoundRect(rectf, radius, radius, borderPaint);
        rectf.inset(lineWidth, lineWidth);
        if (rectf.width() > 0 && rectf.height() > 0) {
            canvas.drawRoundRect(rectf, radius, radius, fillPaint);
        }

        //绘制内部填充

        rectf.left = getPaddingStart() + lineWidth;
        rectf.right = rectf.left + ew;
        rectf.top = getPaddingTop() + lineWidth;
        rectf.bottom = rectf.top + h - 2 * lineWidth;
        mPaint.setColor(shadowColor);
        Path path = new Path();

        rectf.right = w - lineWidth;
        float[] radiiRight = {radius, radius, radius, radius, radius, radius, radius, radius};
        path.addRoundRect(rectf, radius, radius, Path.Direction.CW);

        Path path2 = new Path();
        rectf.right = ew - lineWidth;
        path2.addRect(rectf, Path.Direction.CW);
        path.op(path2, Path.Op.INTERSECT);
        mPaint.setColor(shadowColor);
        canvas.drawPath(path,mPaint);

        //文字绘制
        mPaint.setColor(mainColor);
        mPaint.setTextSize(sp2px(14));
        mPaint.setTextAlign(Paint.Align.CENTER);
        String text = progress + "%";
        float xPos = w / 2.0f;
        float yPos = (h / 2.0f) - ((mPaint.descent() + mPaint.ascent()) / 2.0f);
        canvas.drawText(text, xPos, yPos, mPaint);
    }


    private int dp2px(final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
