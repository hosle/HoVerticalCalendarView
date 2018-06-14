package com.hosle.calendar.verticalcalendar.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.hosle.calendar.verticalcalendar.R
import com.hosle.calendar.verticalcalendar.util.dp2px
import java.util.*



/**
 * Created by tanjiahao on 2018/6/7
 * Original Project VerticalCalendarDemo
 */
class DayOfWeekView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val DAYS_IN_WEEK = 7

    private val paintDayOfWeek = TextPaint()

    private var cellWidth = 0
    private var cellHeight = context.dp2px(20f) //text size

    private val mDayOfWeekLabels = arrayOfNulls<String>(7)
    private var weekStart = 0

    init {
        paintDayOfWeek.isAntiAlias = true
        paintDayOfWeek.textSize = context.dp2px(14f).toFloat()
        paintDayOfWeek.textAlign = Paint.Align.CENTER
        paintDayOfWeek.color = resources.getColor(R.color.color_66_white)
        paintDayOfWeek.style = Paint.Style.FILL
    }

    fun setParams(_weekStart:Int = Calendar.SUNDAY){
        weekStart = if (isValidDayOfWeek(_weekStart)) {
            _weekStart
        } else {
            Calendar.getInstance().firstDayOfWeek
        }

        updateDayOfWeekLabels()

        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddedWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        cellWidth = paddedWidth / DAYS_IN_WEEK

        val paddedHeight = cellHeight + paddingTop + paddingBottom

        val resolvedHeight = View.resolveSize(paddedHeight, heightMeasureSpec)

        setMeasuredDimension(widthMeasureSpec,resolvedHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawDaysOfWeek(canvas!!)
    }

    private fun drawDaysOfWeek(canvas: Canvas) {
        val p = paintDayOfWeek
        val rowHeight = cellHeight
        val colWidth = cellWidth

        val halfLineHeight = (p.ascent() + p.descent()) / 2f
        val rowCenter = rowHeight / 2 + paddingTop

        for (col in 0 until DAYS_IN_WEEK) {
            val colCenter = colWidth * col + colWidth / 2
            val colCenterRtl: Int
            colCenterRtl = colCenter

            val label = mDayOfWeekLabels[col]
            canvas.drawText(label, colCenterRtl.toFloat(), rowCenter - halfLineHeight, p)
        }
    }

    private fun updateDayOfWeekLabels() {
        val tinyWeekdayNames = arrayOf("日","一","二","三","四","五","六")
        for (i in 0 until DAYS_IN_WEEK) {
            mDayOfWeekLabels[i] = tinyWeekdayNames[(weekStart + i - 1) % DAYS_IN_WEEK]
        }
    }

    private fun isValidDayOfWeek(day: Int): Boolean {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY
    }
}