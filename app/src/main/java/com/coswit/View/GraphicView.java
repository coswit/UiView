package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhengjing on 2019-10-18.
 */
public class GraphicView extends View {

    /**
     * 主填充色
     */
    private final int mainColor = 0xFF008AFF;
    private Paint mPaint;
    private Context mContext;
    private int distance = 90;
    private int count = 7;

    /**
     * 坐标描述字体大小sp
     */
    private int descTSize = 12;

    /**
     * text 距离坐标线的距离dp
     */
    private int textTopDistance = 8;

    private float maxValue = 20;

    private Path mPath;

    private List<PointF> pointFList = new ArrayList<>();

    List<PointF> mControlPointList = new ArrayList<>();

    private final float SMOOTHNESS = 0.4f;

    private List<PointInfo> pointsInfo;

    /**
     * 绘制坐标线的宽高
     */
    private float mLineWidth = 1;
    private float lineHeight = 200;


    public GraphicView(Context context) {
        this(context, null);
    }

    public GraphicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineWidth = dp2px(mLineWidth);
        lineHeight = dp2px(lineHeight);
        textTopDistance = dp2px(textTopDistance);
        descTSize = sp2px(descTSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.reset();
        float lineWidth =mLineWidth;
        if (pointsInfo != null && pointsInfo.size() > 0) {
            int px;
            px = getPaddingStart();
            lineWidth += getPaddingStart();
            int measuredHeight = getMeasuredHeight();
            int topLine = getPaddingTop();
            int bottomLine = measuredHeight - descTSize - textTopDistance - getPaddingBottom();

            int lineHeight = bottomLine - topLine;
            float ratio = lineHeight / maxValue;
            float bottomText = measuredHeight - getPaddingBottom();

            pointFList.clear();
            for (int i = 0; i < pointsInfo.size(); i++) {
                mPaint.reset();
                PointInfo pointInfo = pointsInfo.get(i);
                //字体绘制
                mPaint.setColor(0xFF939CA5);
                mPaint.setTextSize(descTSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(pointInfo.desc, px, bottomText, mPaint);

                //坐标线绘制
                mPaint.setColor(0xFFF7F7F7);
                canvas.drawRect(px, topLine, lineWidth, bottomLine, mPaint);

                float py = topLine + pointInfo.value * ratio;

                //中心渐变圆点
                RadialGradient shadow = new RadialGradient(px, py, dp2px(8), mainColor, 0xFFFFFFFF, Shader.TileMode.CLAMP);
                mPaint.setShader(shadow);
                canvas.drawCircle(px, py, dp2px(8), mPaint);
                mPaint.reset();
                mPaint.setColor(0xFFFFFFFF);
                canvas.drawCircle(px, py, dp2px(6), mPaint);
                mPaint.setColor(mainColor);
                canvas.drawCircle(px, py, dp2px(4), mPaint);

                pointFList.add(new PointF(px, py));

                //当前数值显示
                mPaint.setColor(0xFF2A2A2A);
                mPaint.setTextSize(sp2px(12));
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(String.valueOf(pointInfo.value), px, py - dp2px(10), mPaint);

                px += distance;
                lineWidth += distance;
            }
            drawBezier(pointFList, canvas);
        }
    }


    private void drawBezier(List<PointF> pointList, Canvas canvas) {
        mControlPointList.clear();
        if (pointList.size() <= 1) {
            return;
        }
        for (int i = 0; i < pointList.size(); i++) {
            PointF point = pointList.get(i);
            if (i == 0) {
                //第一项
                //添加后控制点
                PointF nextPoint = pointList.get(i + 1);
                float ctrX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float ctrlY = point.y;
                mControlPointList.add(new PointF(ctrX, ctrlY));
            } else if (i == pointList.size() - 1) {//最后一项
                //添加前控制点
                PointF lastPoint = pointList.get(i - 1);
                float ctrX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float ctrY = point.y;
                mControlPointList.add(new PointF(ctrX, ctrY));
            } else {//中间项
                PointF lastPoint = pointList.get(i - 1);
                PointF nextPoint = pointList.get(i + 1);
                float k = (nextPoint.y - lastPoint.y) / (nextPoint.x - lastPoint.x);
                float b = point.y - k * point.x;
                //添加前控制点
                float lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS;
                float lastControlY = k * lastControlX + b;
                mControlPointList.add(new PointF(lastControlX, lastControlY));
                //添加后控制点
                float nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS;
                float nextControlY = k * nextControlX + b;
                mControlPointList.add(new PointF(nextControlX, nextControlY));
            }
        }

        //连接各部分曲线
        mPath = new Path();
        PointF firstPoint = pointList.get(0);
        int height = getMeasuredHeight();

        mPath.moveTo(firstPoint.x, firstPoint.y);
//        mPath.moveTo(firstPoint.x, height);
//        mPath.lineTo(firstPoint.x, firstPoint.y);
        for (int i = 0; i < pointList.size() * 2 - 2; i = i + 2) {
            PointF leftControlPoint = mControlPointList.get(i);
            PointF rightControlPoint = mControlPointList.get(i + 1);
            PointF rightPoint = pointList.get(i / 2 + 1);
            mPath.cubicTo(leftControlPoint.x, leftControlPoint.y, rightControlPoint.x, rightControlPoint.y, rightPoint.x, rightPoint.y);
        }
        //曲线绘制
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mainColor);
        canvas.drawPath(mPath, mPaint);

        PointF lastPoint = pointList.get(pointList.size() - 1);
        //填充渐变色
        mPath.lineTo(lastPoint.x, height);
        mPath.lineTo(firstPoint.x, height);
        mPaint.setAlpha(60);
        mPaint.setStyle(Paint.Style.FILL);
        LinearGradient linearGradient = new LinearGradient(0F, 0F, 0F, getHeight(), mainColor, 0, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        canvas.drawPath(mPath, mPaint);
    }

    public static class PointInfo {
        String desc;
        int value;

        public PointInfo(String desc, int value) {
            this.desc = desc;
            this.value = value;
        }
    }

    public void setData(List<PointInfo> pointsInfo, int maxValue) {
        this.maxValue = maxValue;
        this.pointsInfo = pointsInfo;
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

    float lastx;
    float lasty;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastx = x;
                lasty = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastx;
                float dy = y - lasty;
                if (Math.abs(dx) >= Math.abs(dy)) {
                    scrollBy((int) -dx, 0);
                }
                lasty = y;
                lastx = x;
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }


}
