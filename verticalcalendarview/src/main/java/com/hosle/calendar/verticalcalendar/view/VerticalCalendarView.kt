package com.hosle.calendar.verticalcalendar.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.hosle.calendar.verticalcalendar.R
import com.hosle.calendar.verticalcalendar.util.dp2px
import kotlinx.android.synthetic.main.layout_calendar_view.view.*
import java.util.*


/**
 * Created by tanjiahao on 2018/6/6
 * Original Project VerticalCalendar
 */
class VerticalCalendarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var dateMonth : Array<Array<Int>> = arrayOf()

    private var firstVisibleItemPosition = 0
    private val tvMonthLabel: TextView

    private val monthLabelHeight = context.dp2px(20f) + 10

    private var initialTopOfMonthLabel: Int = Int.MIN_VALUE
    private var initialBottomOfMonthLabel: Int = Int.MIN_VALUE
    private var initialLeftOfMonthLabel: Int = Int.MIN_VALUE
    private var initialRightOfMonthLabel: Int = Int.MIN_VALUE

    init {
        initView(context)
        tvMonthLabel = tv_month_label

    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_calendar_view, this, true)
        header_day_of_week.setParams()
        initRecyclerView()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (initialTopOfMonthLabel == Int.MIN_VALUE)
            initialTopOfMonthLabel = tvMonthLabel.top
        if (initialBottomOfMonthLabel == Int.MIN_VALUE)
            initialBottomOfMonthLabel = tvMonthLabel.bottom
        if (initialLeftOfMonthLabel == Int.MIN_VALUE)
            initialLeftOfMonthLabel = tvMonthLabel.left
        if (initialRightOfMonthLabel == Int.MIN_VALUE)
            initialRightOfMonthLabel = tvMonthLabel.right
    }

    private fun initRecyclerView() {


        recycler_view_calendar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                    val _firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val firstVisibleMonthView = recyclerView.findViewHolderForLayoutPosition(_firstVisibleItemPosition).itemView as MonthView

                    if (_firstVisibleItemPosition != firstVisibleItemPosition) {
                        firstVisibleItemPosition = _firstVisibleItemPosition

                        tvMonthLabel.text = "${firstVisibleMonthView.getMonth()}月"

                        if (isGoingDown(dy)) {
                            tvMonthLabel.layout(initialLeftOfMonthLabel, initialTopOfMonthLabel - monthLabelHeight, initialRightOfMonthLabel, initialBottomOfMonthLabel - monthLabelHeight)
                        } else {
                            tvMonthLabel.layout(initialLeftOfMonthLabel, initialTopOfMonthLabel, initialRightOfMonthLabel, initialBottomOfMonthLabel)
                        }

                    } else {
                        val nextMonthView = recyclerView.findViewHolderForLayoutPosition(firstVisibleItemPosition + 1).itemView as MonthView

                        val targetTop: Int
                        val targetBottom: Int

                        if (nextMonthView.top <= monthLabelHeight) {

                            targetTop = Math.min(tvMonthLabel.top - dy, initialTopOfMonthLabel)
                            targetBottom = Math.min(tvMonthLabel.bottom - dy, initialBottomOfMonthLabel)

                        } else {
                            targetTop = initialTopOfMonthLabel
                            targetBottom = initialBottomOfMonthLabel
                        }
                        tvMonthLabel.layout(initialLeftOfMonthLabel, targetTop, initialRightOfMonthLabel, targetBottom)

                    }
                }
            })
        }
    }

    private fun isGoingDown(dy: Int): Boolean {
        return dy < 0
    }

    fun setCalendarParams(monthArrange:Array<Array<Int>>,onDayClickListener: MonthView.OnDayClickListener?, operationForTaskCount:((Calendar) -> Int)? = null) {
        dateMonth = monthArrange
        setAdapter(CalendarAdapter(dateMonth, onDayClickListener,operationForTaskCount))
    }

    fun setOndayClickListener(onDayClickListener: MonthView.OnDayClickListener?) {
        (recycler_view_calendar.adapter as CalendarAdapter).onDayClickListener = onDayClickListener
        recycler_view_calendar.adapter.notifyDataSetChanged()
    }

    private fun setAdapter(_adapter: CalendarAdapter) {
        recycler_view_calendar.apply {
            adapter = _adapter
        }
    }


    class CalendarAdapter(val monthArange: Array<Array<Int>>, var onDayClickListener: MonthView.OnDayClickListener?,val operationForTaskCount:((Calendar) -> Int)? = null) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_single_month, parent, false) as MonthView
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return monthArange.size
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder!!.root.setMonthParams(monthArange[position][1], monthArange[position][0], operation = operationForTaskCount)
            holder.root.setOnDayClickListener(onDayClickListener)
        }

    }

    class ViewHolder(val root: MonthView) : RecyclerView.ViewHolder(root)

}