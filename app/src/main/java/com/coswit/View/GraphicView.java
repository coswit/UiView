package com.coswit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.coswit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zhengjing on 2019-10-18.
 */
public class GraphicView extends View {

    private Paint mPaint;
    private Context mContext;
    private int distance = 90;
    private int count = 7;

    private int textSize = 22;
    //text 距离线的距离
    private int textTopDistance = 5;

    private float maxValue = 20;

    private float radius = 8;


    private List<Bezier> bezierPoints = new ArrayList<>();
    private Path path;
    private Path mPath;

    private List<PointF> pointFList = new ArrayList<>();

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


    }

    //渐变控制
    private float pathWidth;
    private float pathHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        int lineWidth = 2;
        int left;

        left = getPaddingStart();
        lineWidth += getPaddingStart();

        int measuredHeight = getMeasuredHeight();

        int topLine = getPaddingTop();
        int bottomLine = measuredHeight - textSize - textTopDistance - getPaddingBottom();

        int lineHeight = bottomLine - topLine;
        float ratio = lineHeight / maxValue;

        float bottomText = measuredHeight - getPaddingBottom();


        List<PointsInfo> pointsInfo = getData();

        bezierPoints.clear();
        pointFList.clear();
        for (int i = 0; i < pointsInfo.size(); i++) {
            PointsInfo pointInfo = pointsInfo.get(i);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(textSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(pointInfo.xInfo, left, bottomText, mPaint);

            canvas.drawRect(left, topLine, lineWidth, bottomLine, mPaint);

            float circleY = topLine + pointInfo.yValue * ratio;

            //圆点
            mPaint.setColor(Color.BLUE);
            canvas.drawCircle(left, circleY, radius + 3.5f, mPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(left, circleY, radius + 3, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawCircle(left, circleY, radius, mPaint);

            bezierPoints.add(new Bezier(left, circleY, pointInfo.yValue));
            pointFList.add(new PointF(left, circleY));


            left += distance;

            pathWidth = lineWidth;

            lineWidth += distance;


        }
        pathHeight = bottomLine;
//        drawBezier(canvas);

//        drawCubicBezier(bezierPoints, canvas);
//        drawBezier(bezierPoints, canvas);
        drawValues(canvas);
        calculateControlPoint(pointFList, canvas);
    }

    private List<PointsInfo> getData() {
        List<PointsInfo> pointsInfo = new ArrayList<>();
        pointsInfo.add(new PointsInfo("34周", 10));
        pointsInfo.add(new PointsInfo("35周", 6));
        pointsInfo.add(new PointsInfo("36周", 12));
        pointsInfo.add(new PointsInfo("37周", 14));
        pointsInfo.add(new PointsInfo("38周", 3));
        pointsInfo.add(new PointsInfo("39周", 16));
        return pointsInfo;
    }


    private void drawBezier(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();


        int firstIndex = 0;


        Bezier prevPrev = bezierPoints.get(Math.max(firstIndex - 2, 0));
        Bezier prev = bezierPoints.get(Math.max(firstIndex - 1, 0));

        float preX = 0;
        float preY = 0;
        for (int i = 0; i < bezierPoints.size(); i++) {
            Bezier bezier = bezierPoints.get(i);
            if (i == 0) {
                mPath.moveTo(bezier.x, bezier.y);
            } else {
                float controlX = (preX + bezier.x) / 2;
                float controlY = (preY + bezier.y) / 2 - 100;
                mPath.quadTo(controlX, controlY, bezier.x, bezier.y);
            }
            preX = bezier.x;
            preY = bezier.y;
        }


        canvas.drawPath(mPath, mPaint);

//        float startX = getPaddingStart();
//        float endX = getWidth() - getPaddingEnd();

        drawChatShader(canvas);


    }


    protected void drawCubicBezier(List<Bezier> dataSet, Canvas canvas) {

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();

        //(the effect intensity) Max = 1f = very cubic, Min = 0.05f = low cubic effect, Default: 0.2f
        float intensity = 0.2f;

        mPath.reset();

        float prevDx = 0f;
        float prevDy = 0f;
        float curDx = 0f;
        float curDy = 0f;

        // Take an extra point from the left, and an extra from the right.
        // That's because we need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing weird stuff on the edges of the chart.
        // So in the starting `prev` and `cur`, go -2, -1
        // And in the `lastIndex`, add +1

        final int firstIndex = 0;
        final int lastIndex = dataSet.size() - 1;

        Bezier prevPrev;
        Bezier prev = dataSet.get(Math.max(firstIndex - 2, 0));
        Bezier cur = dataSet.get(Math.max(firstIndex - 1, 0));
        Bezier next = cur;
        int nextIndex = -1;

        if (cur == null) return;

        // let the spline start
        mPath.moveTo(cur.x, cur.y);

        for (int j = 0; j < dataSet.size(); j++) {

            prevPrev = prev;
            prev = cur;
            cur = nextIndex == j ? next : dataSet.get(j);

            nextIndex = j + 1 < dataSet.size() ? j + 1 : j;
            next = dataSet.get(nextIndex);

            prevDx = (cur.y - prevPrev.x) * intensity;
            prevDy = (cur.y - prevPrev.y) * intensity;
            curDx = (next.y - prev.x) * intensity;
            curDy = (next.y - prev.y) * intensity;

            if (j > 0) {

                mPath.cubicTo(prev.x + prevDx, (prev.y + prevDy),
                        cur.x - curDx, (cur.y - curDy),
                        cur.x, cur.y);
            }
        }


        canvas.drawPath(mPath, mPaint);


        drawChatShader(canvas);
    }


    //渐变绘制
    private void drawChatShader(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath.lineTo(pathWidth, pathHeight);
        mPath.lineTo(getPaddingStart(), pathHeight);
        mPath.close();
        int startColor = ContextCompat.getColor(mContext, R.color.transparent);
        int endColor = ContextCompat.getColor(mContext, R.color.transparent_60);
        LinearGradient gradient = new LinearGradient(0, 0, 0, getHeight(), endColor, startColor, Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        canvas.drawPath(mPath, mPaint);
    }


    //
    private void drawValues(Canvas c) {
        float offSet = 20;
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(18);
        mPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < bezierPoints.size(); i++) {
            Bezier b = bezierPoints.get(i);
            c.drawText(b.getValue() + "", b.x, b.y - offSet, mPaint);
        }

    }

    class PointsInfo {
        String xInfo;
        float yValue;

        PointsInfo(String xInfo, float yValue) {
            this.xInfo = xInfo;
            this.yValue = yValue;
        }
    }

    class Bezier extends PointF {
        float value;

        public Bezier() {
        }

        Bezier(float x, float y, float yValue) {
            this.x = x;
            this.y = y;
            this.value = yValue;
        }


        float getValue() {
            return value;
        }
    }


    List<PointF> mControlPointList = new ArrayList<>();
    float SMOOTHNESS = 0.4f;

    private void calculateControlPoint(List<PointF> pointList, Canvas canvas) {
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
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath,mPaint);

        PointF lastPoint = pointList.get(pointList.size() - 1);
        //填充渐变色
        mPath.lineTo(lastPoint.x, height);
        mPath.lineTo(firstPoint.x, height);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL);
        LinearGradient linearGradient = new LinearGradient(0F, 0F, 0F, getHeight(), Color.RED, 0, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        canvas.drawPath(mPath, mPaint);
//        //绘制全部路径
//        mPath.setLastPoint(lastPoint.x, height);
//        mPaint.setStrokeWidth(5);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setShader(null);
//        mPaint.setColor(Color.RED);
//        canvas.drawPath(mPath, mPaint);
        for (int i = 0; i < pointList.size(); i++) {
            PointF point = pointList.get(i);
            //画数值线
//            mPaint.setColor(Color.BLUE);
//            mPaint.setAlpha(100);
//            canvas.drawLine(point.x, point.y, point.x, height, mPaint);
            //画数值点
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(255);
            canvas.drawCircle(point.x, point.y, 20, mPaint);
        }

    }
}
