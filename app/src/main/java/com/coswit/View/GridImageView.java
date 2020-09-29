package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coswit.R;

/**
 * @author Created by zhengjing on 2020/9/21.
 */
public class GridImageView extends ViewGroup {
    /**
     * 每行显示个数
     */
    private final int maximum = 3;

    private Context context;
    private int cubeSize;
    private int gapSize;
    private LayoutParams imageParams;
    /**
     * 是否为全部铺开展示
     */
    private boolean isSpread;

    public GridImageView(Context context) {
        this(context, null);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        gapSize = dp2px(6);
        imageParams = new LayoutParams(cubeSize, cubeSize);
        setImages(4, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int rows = (int) Math.ceil(childCount / (maximum * 1f));
        int linePadding = gapSize * Math.max(0, rows - 1);
        int remainWidth = width - getPaddingStart() - getPaddingEnd();
        cubeSize = (remainWidth - 2 * gapSize) / 3;
        int height = cubeSize * rows + linePadding + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingTop = getPaddingTop();
        //4张时固定位置
        if (childCount == maximum + 1 && !isSpread) {
            int top = paddingTop;
            int start = paddingStart;
            View child0 = getChildAt(0);
            child0.layout(start, top, start + cubeSize, top + cubeSize);
            View child2 = getChildAt(2);
            child2.layout(start, top + gapSize + cubeSize, start + cubeSize, top + gapSize + cubeSize + cubeSize);
            start = start + cubeSize + gapSize;
            View child1 = getChildAt(1);
            child1.layout(start, top, start + cubeSize, top + cubeSize);
            View child3 = getChildAt(3);
            top = top + gapSize + cubeSize;
            child3.layout(start, top, start + cubeSize, top + cubeSize);
        } else {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                //当前所处行位置
                int row = (int) Math.ceil((i + 1) / (maximum * 1f));
                //当前所处列位置-1
                int column = i % maximum;
                int start = paddingStart + cubeSize * column + gapSize * column;
                int top = paddingTop + cubeSize * Math.max(0, row - 1) + gapSize * Math.max(0, row - 1);
                child.layout(start, top, start + cubeSize, top + cubeSize);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public void setImages(int size, boolean isSpread) {
        this.isSpread = isSpread;
        removeAllViews();

        if (!isSpread && size > 6) {
            for (int i = 0; i < 5; i++) {
                CustomImageView item = new CustomImageView(context);
                item.setScaleType(ImageView.ScaleType.FIT_XY);
                item.setLayoutParams(imageParams);
                Glide.with(context).load(R.mipmap.logo).into(item);
                addView(item);
            }
            String text = "+" + (size - 6);
            CustomImageView item = new CustomImageView(context, text);
            item.setMinimumWidth(cubeSize);
            item.setScaleType(ImageView.ScaleType.FIT_XY);
            item.setLayoutParams(imageParams);
            Glide.with(context).load(R.mipmap.logo).into(item);
            addView(item);
        } else {
            for (int i = 0; i < size; i++) {
                CustomImageView item = new CustomImageView(context);
                item.setScaleType(ImageView.ScaleType.FIT_XY);
                item.setLayoutParams(imageParams);
                Glide.with(context).load(R.mipmap.logo).into(item);
                addView(item);
            }
        }
    }

    private int dp2px(final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {
        private Context context;
        private Paint mPaint;
        private String text;
        private float x;
        private float y;

        public CustomImageView(Context context, String text) {
            super(context);
            this.text = text;
            init(context);
        }

        public CustomImageView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            this.context = context;
            mPaint = new Paint();
        }


        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            x = (right - left) / 2f;
            y = (bottom - top) / 2f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (text != null && !text.isEmpty()) {
                mPaint.reset();
                canvas.drawColor(0x99000000);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setColor(0xFFFFFFFF);
                int textSize = sp2px(30);
                mPaint.setTextSize(textSize);
                canvas.drawText(text, x, y + textSize / 3f, mPaint);
            }
        }

        private int sp2px(final float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * fontScale + 0.5f);
        }


    }

}
