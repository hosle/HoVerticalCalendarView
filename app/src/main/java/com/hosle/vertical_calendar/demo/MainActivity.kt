package com.hosle.vertical_calendar.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hosle.calendar.verticalcalendar.view.MonthView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private val dayTask:HashMap<String,Int> = hashMapOf("2018-06-08" to 1,"2018-06-09" to 2,"2018-05-30" to 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar_view.setCalendarParams(createMonth(4), object : MonthView.OnDayClickListener {
            override fun onDayClick(view: MonthView, day: Calendar) {
                val dateString = "${day.get(Calendar.YEAR)}-${day.get(Calendar.MONTH) + 1}-${day.get(Calendar.DAY_OF_MONTH)}"
                Toast.makeText(this@MainActivity, dateString, Toast.LENGTH_SHORT).show()

            }

        }, { calendar ->
            run {
                val dateString = "${calendar.get(Calendar.YEAR)}-${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}-${String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))}"

                dayTask[dateString] ?: 0
            }
        })
    }

    private fun createMonth(count:Int):Array<Array<Int>>{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH,-1)

        val resultList = ArrayList<Array<Int>>()

        for(i in 0 until count){
            val item = arrayOf(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1)
            resultList.add(item)
            calendar.add(Calendar.MONTH, 1)
        }

        return resultList.toTypedArray()
    }
}
