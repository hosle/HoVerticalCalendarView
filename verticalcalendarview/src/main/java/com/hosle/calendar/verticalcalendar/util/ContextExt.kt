package com.hosle.calendar.verticalcalendar.util

import android.content.Context

/**
 * Created by tanjiahao on 2018/6/7
 * Original Project VerticalCalendarDemo
 */
fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}