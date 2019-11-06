package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author Created by zhengjing on 2019-11-05.
 */
public class StatisticTabsView extends LinearLayout {

    private Paint mPaint;
    private Context mContext;
    private RectF rectF;

    /**
     * 选中底部宽高 dp
     */
    private float slWidth = 20;
    private float slHeight = 2;

    private int postion;

    private float radius = 2;

    String[] titles = {"做题数", "正确率", "用时"};

    private OnTabSelectListener listener;

    public StatisticTabsView(Context context) {
        this(context, null);
    }

    public StatisticTabsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatisticTabsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        setOrientation(HORIZONTAL);
        setWillNotDraw(false);
        mPaint = new Paint();
        rectF = new RectF();
        setGravity(Gravity.CENTER_VERTICAL);
        slWidth = dp2px(slWidth);
        slHeight = dp2px(slHeight);

        setTitles(titles);
    }

    public void setTitles(String[] titles) {
        if (titles != null && titles.length > 0) {
            removeAllViews();
            for (int i = 0; i < titles.length; i++) {
                TextView textView = new TextView(mContext);
                textView.setPadding((int)dp2px(5), (int)dp2px(5),(int) dp2px(5), (int)dp2px(3));
                textView.setText(titles[i]);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                addView(textView);
                final int pos = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StatisticTabsView.this.listener != null) {
                            listener.onTabSelect(pos);
                        }
                        postion = pos;
                        invalidate();
                    }
                });
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //底部灰线绘制
        float height = getMeasuredHeight();
        float width = getMeasuredWidth();
        mPaint.setColor(0xFFECECEC);
        float lineHeight = dp2px(1);
        canvas.drawRect(0, height, width, height - lineHeight, mPaint);

        //底部选择线绘制
        int childCount = getChildCount();
        if (childCount < postion) {
            return;
        }
        //子view宽度
        float pWidth = width / (childCount * 1.0f);
        mPaint.setColor(0xFF008AFF);
        float gap = (pWidth - slWidth) / 2f;
        rectF.left = pWidth * postion * 1f + gap;
        rectF.right = rectF.left + slWidth;
        rectF.top = height;
        rectF.bottom = height - slHeight;
        canvas.drawRoundRect(rectF, dp2px(radius), dp2px(radius), mPaint);

        resetTextColor();

    }

    private void resetTextColor(){
        int childCount = getChildCount();
        for(int i = 0;i<childCount;i++) {
            View child = getChildAt(i);
            if(child instanceof TextView){
                ((TextView) child).setTextColor(i==postion?0xFF2A2A2A:0xFF939CA5);
            }
        }
    }

    public float dp2px(final float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }


    public interface OnTabSelectListener {
        void onTabSelect(int position);
    }

    public void setTabSelectListener(OnTabSelectListener listener) {
        this.listener = listener;
    }
}
