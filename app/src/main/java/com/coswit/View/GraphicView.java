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
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static androidx.customview.widget.ViewDragHelper.INVALID_POINTER;

/**
 * @author Created by zhengjing on 2019-10-18.
 */
public class GraphicView extends FrameLayout {

    /**
     * 主填充色
     */
    private final int mainColor = 0xFF008AFF;
    private Paint mPaint;
    private Context mContext;
    /**
     * 坐标线之间的距离
     */
    private int distance = 52;

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

    private final float SMOOTHNESS = 0.3f;

    private List<PointInfo> pointsInfo;

    /**
     * 绘制坐标线的宽高
     */
    private final float LINE_WITH = 1;
    private float LINE_HEIGHT = 200;

    /**
     * 顶部数值距离点的距离dp
     */
    private static final int TOP_TEXT_MARGIN = 10;
    //数值显示大小
    private static final int TOP_TEXT_SIZE = 12;

    /**
     * 绘制最终的宽度
     */
    private float mLWidth;

    /**
     * 滑动相关处理
     */
    private int mLastMotionX;
    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;
    private int mOverscrollDistance;
    private boolean mIsBeingDragged;
    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    int mOverflingDistance;
    //最大回弹距离
    private static final int OFF_SET = 200;

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
        LINE_HEIGHT = dp2px(LINE_HEIGHT);
        textTopDistance = dp2px(textTopDistance);
        descTSize = sp2px(descTSize);
        distance = dp2px(distance);

        initScroll();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        resetPaint();
        mPaint.setAntiAlias(true);
        mLWidth = dp2px(LINE_WITH);
        if (pointsInfo != null && pointsInfo.size() > 0) {
            int px = getPaddingStart();
            mLWidth += getPaddingStart();
            int measuredHeight = getMeasuredHeight();
            int topLine = getPaddingTop() + dp2px(TOP_TEXT_MARGIN) + sp2px(TOP_TEXT_SIZE);
            int bottomLine = measuredHeight - descTSize - textTopDistance - getPaddingBottom();

            int lineHeight = bottomLine - topLine;
            float ratio = lineHeight / maxValue;
            float bottomText = measuredHeight - getPaddingBottom();

            pointFList.clear();
            for (int i = 0; i < pointsInfo.size(); i++) {
                resetPaint();
                PointInfo pointInfo = pointsInfo.get(i);
                //字体绘制
                mPaint.setColor(0xFF939CA5);
                mPaint.setTextSize(descTSize);
                mPaint.setTextAlign(Paint.Align.CENTER);

                canvas.drawText(pointInfo.desc, px, bottomText, mPaint);

                //坐标线绘制
                mPaint.setColor(0xFFF7F7F7);
                canvas.drawRect(px, topLine, mLWidth, bottomLine, mPaint);

                float py = bottomLine - pointInfo.value * ratio;

                //中心渐变圆点
                RadialGradient shadow = new RadialGradient(px, py, dp2px(8), mainColor, 0xFFFFFFFF, Shader.TileMode.CLAMP);
                mPaint.setShader(shadow);
                canvas.drawCircle(px, py, dp2px(8), mPaint);
                resetPaint();
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
                //最后绘制不计入宽度
                if (i != pointsInfo.size() - 1) {
                    mLWidth += distance;
                }
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


    private static final String TAG = "gra_tag";

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                initOrResetVelocityTracker();
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                mIsBeingDragged = !mScroller.isFinished();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mActivePointerId = ev.getPointerId(0);
                mLastMotionX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final int x = (int) ev.getX(activePointerIndex);
                int deltaX = mLastMotionX - x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mVelocityTracker.addMovement(ev);
                    mLastMotionX = x;
                    int scrollRange = getScrollRange();
                    if (overScrollBy(deltaX, 0, getScrollX(), 0, scrollRange, 0,
                            mOverscrollDistance, 0, true)) {
                        mVelocityTracker.clear();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                    mVelocityTracker.addMovement(ev);
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        fling(-initialVelocity);
                    } else {
                        //处理回弹
                        if (getScrollX() < 0) {
                            scrollTo(0, getScrollY());
                        } else if (getScrollX() > getScrollRange()) {
                            scrollTo(getScrollRange(), getScrollY());
                        }
                    }
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void resetPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
    }
    private void initScroll() {
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mScroller = new OverScroller(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS);
    }

    private int getScrollRange() {
        float range = Math.max(0,
                mLWidth - (getWidth() - getPaddingLeft() - getPaddingRight()));
        return (int) range;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }


    //处理惯性滑动
    public void fling(int velocityX) {
        mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0,
                getScrollRange(), 0, 0, getScrollRange() / 2, 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int x = mScroller.getCurrX();
            if (oldX != x) {
                final int range = getScrollRange();
                overScrollBy(x - oldX, 0, oldX, 0, range, 0,
                        mOverflingDistance, 0, false);

            }
        }
    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (isTouchEvent) {
            maxOverScrollX += OFF_SET;
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    @Override
    protected int computeHorizontalScrollRange() {
        int scrollX = getScrollX();
        int width = getWidth();
        if (scrollX <= 0) {
            return width - scrollX + OFF_SET;
        } else if (scrollX >= getScrollRange()) {
            return width + scrollX;
        }
        return width;
    }


}
