package com.example.spendwithbrain.screens.main.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.models.UserWithExpenses
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.DateUtils
import com.example.spendwithbrain.utils.SharedPrefUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_budget.*
import kotlinx.android.synthetic.main.fragment_budget.view.*
import java.util.*
import kotlin.collections.ArrayList


class BudgetFragment : Fragment() {
    private lateinit var layout: View
    private lateinit var balanceValue: TextView
    private lateinit var todayExpenseValue: TextView
    private lateinit var weekExpenseValue: TextView
    private lateinit var monthExpenseValue: TextView
    private lateinit var barChart: BarChart
    private var chartMap: LinkedHashMap<String, Double> = linkedMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_budget, container, false)
        initViewComponents(layout)
        return layout
    }

    @Override
    override fun onResume() {
        super.onResume()
        initComponents()
        val userDetails = loadUserWithExpenses()
        var endDate = System.currentTimeMillis()
        for (i in 0..11) {
            val startDate = DateUtils.getOneMonthAgoTime(endDate)
            chartMap[DateUtils.getMonthName(startDate)] =
                computeMonthCashFlow(userDetails, startDate, endDate)
            endDate = startDate - 1000
        }
        setUpChart()
    }

    private fun loadUserWithExpenses(): UserWithExpenses {
        return RoomDb.db.userDetailsDAO()
            .getUserExpenses(
                activity!!.getSharedPreferences(
                    Constants.MY_SHARED_PREFERENCE,
                    Constants.PRIVATE_MODE
                ).getInt(Constants.USER_ID, -1)
            )
    }

    private fun computeMonthCashFlow(
        userWithExpenses: UserWithExpenses,
        startDate: Long,
        endDate: Long
    ): Double {
        val expensesSum =
            userWithExpenses.expenses.filter { ex -> ex.expensesDate in startDate..endDate }
                .sumByDouble { it.expensesAmount.toDouble() }
        val incomeSum =
            userWithExpenses.incomes.filter { ex -> ex.incomeDate in startDate..endDate }
                .sumByDouble { it.incomeAmount.toDouble() }

        return incomeSum - expensesSum
    }

    private fun initViewComponents(view: View) {
        balanceValue = view.balance_value_textView
        todayExpenseValue = view.today_expense_value_textView
        weekExpenseValue = view.week_expense_value_textView
        monthExpenseValue = view.month_expense_value_textView
        barChart = view.details_barChart
    }

    private fun initComponents() {
        SharedPrefUtils.init(activity!!)

        val startDay = Calendar.getInstance().timeInMillis - (24 * 60 * 60 * 1000)
        val endDay = Calendar.getInstance().timeInMillis

        val c: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        c.add(Calendar.MONTH, -1)
        val startMonth = c.timeInMillis

        val c1: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        c1.add(Calendar.WEEK_OF_MONTH, -1)
        val startWeek = c1.timeInMillis

        if (SharedPrefUtils.hasKey(Constants.USER_ID)) {
            Thread {
                val userId = SharedPrefUtils.read(Constants.USER_ID, -1)
                val userBalance = RoomDb.db.userDetailsDAO()
                    .getUserBalance(userId).toString()

                val expensesByDate = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startDay,
                    endDay,
                    userId
                ).toString()

                val expensesByWeek = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startWeek,
                    endDay,
                    userId
                ).toString()

                val expensesByMonth = RoomDb.db.expenseDetailsDAO().getUserExpenseByDate(
                    startMonth,
                    endDay,
                    userId
                ).toString()

                this.activity?.let { activity ->
                    Handler(activity.mainLooper).post {
                        balanceValue.text = userBalance
                        today_expense_value_textView.text = expensesByDate
                        weekExpenseValue.text = expensesByWeek
                        monthExpenseValue.text = expensesByMonth
                    }
                } ?: kotlin.run {
                    Log.e("Activity null", "Fragment's activity is null")
                }
            }.start()
        }
    }

    private fun setUpChart() {
        val list: ArrayList<BarEntry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()

        val balance = mutableListOf<Double>()
        val xAxisLabel = mutableListOf<String>()

        chartMap.forEach { (s, d) ->
            xAxisLabel.add(s)
            balance.add(d)
        }
        xAxisLabel.reverse()
        balance.reverse()

        for (i in balance.indices) {
            list.add(BarEntry(i.toFloat(), balance[i].toFloat()))
            if (balance[i] >= 0) {
                colors.add(resources.getColor(R.color.colorChartGreen))
            } else {
                colors.add(resources.getColor(R.color.colorRed))
            }
        }

        setAppearanceForChart(xAxisLabel, list, colors)
    }

    private fun setAppearanceForChart(
        xAxisLabel: MutableList<String>,
        list: List<BarEntry>,
        colors: ArrayList<Int>
    ) {
        val barData = BarData() //all the data for chart
        val dataSet = BarDataSet(list, "")

        dataSet.colors = colors
        dataSet.setValueTextColors(colors)
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

        //set up legend
        val colors: Array<Int> = arrayOf<Int>(
            ContextCompat.getColor(context!!, R.color.colorChartGreen),
            ContextCompat.getColor(context!!, R.color.colorRed)
        )
        barChart.legend.isEnabled = true
        barChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        val l1 = LegendEntry(
            getString(R.string.positive_balance),
            Legend.LegendForm.SQUARE,
            10f,
            2f,
            null,
            colors[0]
        )
        val l2 = LegendEntry(
            getString(R.string.negative_balance),
            Legend.LegendForm.SQUARE,
            10f,
            2f,
            null,
            colors[1]
        )
        barChart.legend.setCustom(arrayOf(l1, l2))

        //modify the default appearance
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.description.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.textSize = 10f
        barChart.setExtraOffsets(0f, 0f, 0f, 1f)
        barChart.axisLeft.textSize = 14f
        barChart.data = barData
        barChart.invalidate()
    }
}