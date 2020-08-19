package com.example.spendwithbrain.screens.main.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.utils.Constants
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_budget.*
import java.util.*


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
        setBarChart(view)
        return view
    }

    private fun initViewComponents(view: View) {
        balanceValue = view.findViewById(R.id.balance_value_textView)
        todayExpenseValue = view.findViewById(R.id.today_expense_value_textView)
        weekExpenseValue = view.findViewById(R.id.week_expense_value_textView)
        monthExpenseValue = view.findViewById(R.id.month_expense_value_textView)
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
                todayExpenseValue.text = RoomDb.db.userDetailsDAO().getUserExpenseByDate(
                    startDay,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()

                weekExpenseValue.text = RoomDb.db.userDetailsDAO().getUserExpenseByDate(
                    startWeek,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()

                monthExpenseValue.text = RoomDb.db.userDetailsDAO().getUserExpenseByDate(
                    startMonth,
                    endDay,
                    sharedPreferences.getInt(Constants.USER_ID, -1)
                ).toString()
            }.start()
        }
    }

    private fun setBarChart(view: View) {
        barChart = view.findViewById(R.id.details_barChart)

        val NoOfEmp = ArrayList<BarEntry>()

        NoOfEmp.add(BarEntry(945f, 0.toFloat()))
        NoOfEmp.add(BarEntry(1040f, 1.toFloat()))
        NoOfEmp.add(BarEntry(1133f, 2.toFloat()))
        NoOfEmp.add(BarEntry(1240f, 3.toFloat()))

        val barDataSet = BarDataSet(NoOfEmp, "")
        val data = BarData(barDataSet)
        barDataSet.setColors(resources.getColor(R.color.colorGreen))
        barChart.setData(data)
    }
}