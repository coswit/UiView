package com.coswit.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * @author Created by zhengjing on 2020/12/3.
 */
open class RelativeLayoutProgress : RelativeLayout {

    private var progress = 90f
    private val lineWidth: Float
    private val radius: Float
    private val mPaint: Paint = Paint()
    private val fillColor = 0xFFFFFFFF
    private val shadowColor = 0xFFE5E5E5
    private val lineColor = 0xFFE1E6F0
    private val rectf: RectF = RectF()
    private val borderPaint = Paint()
    private val fillPaint = Paint()


    init {
        lineWidth = dp2px(1f).toFloat()
        radius = dp2px(17f).toFloat()
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(
            context,
            attrs, 0
    )

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = measuredWidth
        val h = measuredHeight
        val ph = h * (progress / 100f)
        mPaint.reset()
        mPaint.isAntiAlias = true

        //填充框线
        borderPaint.reset()
        borderPaint.isAntiAlias = true
        borderPaint.style = Paint.Style.FILL
        borderPaint.color = lineColor.toInt()
        borderPaint.isDither = true

        fillPaint.reset()
        fillPaint.isAntiAlias = true
        fillPaint.style = Paint.Style.FILL
        fillPaint.isDither = true
        fillPaint.color = fillColor.toInt()
        rectf.left =0f
        rectf.right = rectf.left + w
        rectf.top = 0f
        rectf.bottom = rectf.top + h
        canvas.drawRoundRect(rectf, radius, radius, borderPaint)
        rectf.inset(lineWidth, lineWidth)
        if (rectf.width() > 0 && rectf.height() > 0) {
            canvas.drawRoundRect(rectf, radius, radius, fillPaint)
        }

        //绘制内部填充
        rectf.left = lineWidth
        rectf.right = width - lineWidth
        rectf.bottom = h - lineWidth
        rectf.top = rectf.bottom-ph-lineWidth
        mPaint.color = shadowColor.toInt()
        val path = Path()
        val radii = floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
        path.addRoundRect(rectf, radii, Path.Direction.CW)
        val path2 = Path()
        rectf.top = lineWidth
        path2.addRect(rectf, Path.Direction.CW)
        //相交部分
        path.op(path2, Path.Op.INTERSECT)
        canvas.drawPath(path, mPaint)



    }


    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}