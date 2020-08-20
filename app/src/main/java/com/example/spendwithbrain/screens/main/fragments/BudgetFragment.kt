package com.example.spendwithbrain.screens.main.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.utils.Constants
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_budget.*
import kotlinx.android.synthetic.main.home_toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList


class BudgetFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var balanceValue: TextView
    private lateinit var todayExpenseValue: TextView
    private lateinit var weekExpenseValue: TextView
    private lateinit var monthExpenseValue: TextView
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_budget, container, false)

        initViewComponents(view)
        initComponents(view)
        setUpChart()
        return view
    }

    private fun initViewComponents(view: View) {
        balanceValue = view.findViewById(R.id.balance_value_textView)
        todayExpenseValue = view.findViewById(R.id.today_expense_value_textView)
        weekExpenseValue = view.findViewById(R.id.week_expense_value_textView)
        monthExpenseValue = view.findViewById(R.id.month_expense_value_textView)
        barChart = view.findViewById(R.id.details_barChart)
    }

    private fun initComponents(view: View) {
        sharedPreferences =
            activity!!.getSharedPreferences(Constants.MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val startDay = Calendar.getInstance().timeInMillis - (24 * 60 * 60 * 1000)
        val endDay = Calendar.getInstance().timeInMillis

        val c: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        c.add(Calendar.MONTH, -1)
        val startMonth = c.timeInMillis

        val c1: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        c1.add(Calendar.WEEK_OF_MONTH, -1)
        val startWeek = c1.timeInMillis

        if (sharedPreferences.contains(Constants.USER_ID)) {
            Thread {
                balanceValue.text = RoomDb.db.userDetailsDAO()
                    .getUserBalance(sharedPreferences.getInt(Constants.USER_ID, -1)).toString()
                todayExpenseValue.text = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startDay,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()

                weekExpenseValue.text = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startWeek,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()

                monthExpenseValue.text = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startMonth,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()
            }.start()
        }
    }

    private fun setUpChart() {
        val list: ArrayList<BarEntry> = ArrayList()
        val xAxisLabel: ArrayList<String> = ArrayList()

        var dbList: List<ExpensesDetails> = ArrayList()
        if (sharedPreferences.contains(Constants.USER_ID)) {
            Thread{
                dbList = RoomDb.db.expenseDetailsDAO().getExpenseByCategory(sharedPreferences.getInt(Constants.USER_ID, -1))
                for (i in dbList.indices){
                    xAxisLabel.add(dbList[i].expensesCategory)
                    val entry = BarEntry(i.toFloat(), dbList[i].expensesAmount.toFloat())
                    list.add(entry)
                }

                setAppearanceForChart(xAxisLabel, list)
            }.start()
        }
    }

    private fun setAppearanceForChart(xAxisLabel: ArrayList<String>, list: List<BarEntry>) {
        val barData = BarData() //all the data for chart
        val dataSet = BarDataSet(list, "")
        dataSet.color = resources.getColor(R.color.colorChartGreen)
        dataSet.valueTextSize = 10f
        barData.addDataSet(dataSet)
        barData.barWidth = 0.7f

        //set up the x coordinate
        val xAxis = barChart.xAxis
        val yAxis = barChart.axisLeft
        xAxis.labelCount = xAxisLabel.size + 1
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(true)
        yAxis.setDrawGridLines(false)

        //modify the default appearance
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.textSize = 10f
        barChart.setExtraOffsets(0f, 0f, 0f, 1f)
        barChart.axisLeft.textSize = 14f
        barChart.data = barData
        barChart.invalidate()
    }
}