package com.example.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.random.Random

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barChartData: List<Float> = generateData()

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.bar_chart_bar_color)
    }

    private val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.bar_chart_axis_color)
        strokeWidth = resources.getDimension(R.dimen.bar_chart_axis_width)
    }

    private val guidePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.bar_chart_guide_color)
        strokeWidth = resources.getDimension(R.dimen.bar_chart_line_width)
    }

    private var padding = 0f
    private var barSpace = 0f

    private val rect = RectF()

    init {
        context.withStyledAttributes(attrs, R.styleable.BarChartView) {
            padding = getDimension(R.styleable.BarChartView_android_padding, 0f)
            barSpace = getDimension(R.styleable.BarChartView_android_spacing, 0f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        rect.set(padding, padding, width - padding, height - padding)

        drawAxisLines(canvas)
        drawGuideLines(canvas)
        drawBars(canvas)
    }

    private fun drawAxisLines(canvas: Canvas) {
        canvas.drawLine(rect.left, rect.bottom, rect.left, rect.top, axisPaint)
        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, axisPaint)
    }

    private fun drawGuideLines(canvas: Canvas) {
        val guideLineSpace = (rect.bottom - rect.top) / 10

        for (i in 0..9) {
            val lineHeight = rect.top + (i * guideLineSpace)
            canvas.drawLine(rect.left, lineHeight, rect.right, lineHeight, guidePaint)
        }
    }

    private fun drawBars(canvas: Canvas) {
        val totalBarSpace = barSpace * (barChartData.size + 1)
        val barWidth = (rect.right - rect.left - totalBarSpace) / barChartData.size
        var left = rect.left + barSpace
        var right = left + barWidth
        barChartData.forEach {
            val top = rect.top + (rect.height() * (1 - it))
            canvas.drawRect(left, top, right, rect.bottom, barPaint)

            left = right + barSpace
            right = left + barWidth
        }
    }

    private fun generateData(): List<Float> {
        return List((5..15).random()) {
            Random.nextFloat()
        }
    }
}
