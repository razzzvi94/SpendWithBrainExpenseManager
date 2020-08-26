package com.example.spendwithbrain.screens.main.fragments.expenses

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendwithbrain.R
import com.example.spendwithbrain.db.RoomDb
import com.example.spendwithbrain.db.entities.ExpensesDetails
import com.example.spendwithbrain.db.entities.IncomeDetails
import com.example.spendwithbrain.db.models.UserWithExpenses
import com.example.spendwithbrain.screens.main.fragments.expenses.adaptor.TransactionAdapter
import com.example.spendwithbrain.screens.main.fragments.expenses.adaptor.models.Transaction
import com.example.spendwithbrain.utils.Constants
import com.example.spendwithbrain.utils.DateUtils
import com.example.spendwithbrain.utils.SharedPrefUtils
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.trinnguyen.SegmentView
import kotlinx.android.synthetic.main.fragment_expenses.view.*
import java.util.*
import kotlin.collections.ArrayList


class ExpensesFragment : Fragment(), SegmentView.OnSegmentItemSelectedListener {
    private lateinit var layout: View
    private var userId: Int = 0
    private var endDay: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_expenses, container, false)

        initSegmentComponents()
        initComponents()
        initTransactionRecyclerView()

        return layout
    }

    override fun onSegmentItemSelected(index: Int) {
        when (index) {
            0 -> {
                val startWeek = DateUtils.getEndDate(Calendar.WEEK_OF_MONTH)
                layout.total_expense_value.text =
                    RoomDb.db.expenseDetailsDAO().getExpenseInInterval(userId, startWeek, endDay)

                setUpChart(getDataFromDB(userId, startWeek, endDay))
            }
            1 -> {
                val startMonth = DateUtils.getEndDate(Calendar.MONTH)
                layout.total_expense_value.text =
                    RoomDb.db.expenseDetailsDAO().getExpenseInInterval(userId, startMonth, endDay)

                setUpChart(getDataFromDB(userId, startMonth, endDay))
            }
            2 -> {
                val startYear = DateUtils.getEndDate(Calendar.YEAR)
                layout.total_expense_value.text =
                    RoomDb.db.expenseDetailsDAO().getExpenseInInterval(userId, startYear, endDay)

                setUpChart(getDataFromDB(userId, startYear, endDay))
            }
        }
    }

    override fun onSegmentItemReselected(index: Int) {}

    private fun initSegmentComponents() {
        layout.interval_segment_view.setText(0, getString(R.string.this_week))
        layout.interval_segment_view.setText(1, getString(R.string.this_month))
        layout.interval_segment_view.setText(2, getString(R.string.this_year))

        layout.interval_segment_view.onSegmentItemSelectedListener = this
    }

    private fun initComponents() {
        userId = SharedPrefUtils.read(Constants.USER_ID, -1)
        endDay = Calendar.getInstance().timeInMillis

        val startWeek = DateUtils.getEndDate(Calendar.WEEK_OF_MONTH)
        layout.total_expense_value.text =
            RoomDb.db.expenseDetailsDAO().getExpenseInInterval(userId, startWeek, endDay)

        setUpChart(getDataFromDB(userId, startWeek, endDay))
    }

    private fun setUpChart(chartData: MutableList<ExpensesDetails>) {
        val yValue: ArrayList<PieEntry> = ArrayList<PieEntry>()

        for (element in chartData) {
            yValue.add(PieEntry(element.expensesAmount.toFloat(), element.expensesCategory))
        }

        setAppearanceForChart(yValue)
    }

    private fun setAppearanceForChart(yValue: ArrayList<PieEntry>) {
        //data set formatting
        val dataSet = PieDataSet(yValue, "")
        dataSet.sliceSpace = 2F
        dataSet.selectionShift = 5f
        dataSet.setColors(
            Color.rgb(217, 80, 138),
            Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),
            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124)
        )

        var pieData: PieData = PieData(dataSet)
        pieData.setValueTextSize(10f)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueFormatter(PercentFormatter(layout.pieChart))

        //chart
        layout.pieChart.isRotationEnabled = false
        layout.pieChart.isDrawHoleEnabled = true
        layout.pieChart.setHoleColor(Color.WHITE)
        layout.pieChart.transparentCircleRadius = 61f
        layout.pieChart.setEntryLabelColor(Color.WHITE)
        layout.pieChart.data = pieData
        layout.pieChart.invalidate()
        layout.pieChart.setUsePercentValues(true)

        //legend setup
        val legend: Legend = layout.pieChart.legend
        legend.isEnabled = false

        //description setup
        val description: Description = layout.pieChart.description
        description.isEnabled = false
    }

    private fun initTransactionRecyclerView() {
        val userDetails = loadUserWithExpenses()

        val incomeList = mutableListOf<IncomeDetails>()
        incomeList.addAll(userDetails.incomes)

        val expenseList = mutableListOf<ExpensesDetails>()
        expenseList.addAll(userDetails.expenses)

        val transactionList = mutableListOf<Transaction>()
        for (index in 0 until incomeList.size) {
            var transactionName: String = context?.getString(R.string.income) ?: String.toString()

            val transactionObject: Transaction = Transaction(
                incomeList[index].incomeDate,
                incomeList[index].incomeAmount.toDouble(),
                incomeList[index].incomeCategory,
                transactionName,
                0.0
            )
            transactionList.add(transactionObject)
        }

        for (index in 0 until expenseList.size) {
            var transactionName: String = context?.getString(R.string.expense) ?: String.toString()

            val transactionObject: Transaction = Transaction(
                expenseList[index].expensesDate,
                expenseList[index].expensesAmount.toDouble(),
                expenseList[index].expensesCategory,
                transactionName,
                0.0
            )
            transactionList.add(transactionObject)
        }

        var orderedList: List<Transaction> = transactionList.sortedByDescending { it.date }

        calculateDynamicBalance(orderedList, userDetails)

        layout.transactions_recycler.adapter = TransactionAdapter(context!!, orderedList)
        layout.transactions_recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun getDataFromDB(
        userId: Int,
        startDate: Long,
        endDate: Long
    ): MutableList<ExpensesDetails> {
        return RoomDb.db.expenseDetailsDAO()
            .getExpensesByCategoryInInterval(userId, startDate, endDate)
    }

    private fun loadUserWithExpenses(): UserWithExpenses {
        return RoomDb.db.userDetailsDAO()
            .getUserExpenses(SharedPrefUtils.read(Constants.USER_ID, -1))
    }

    private fun calculateDynamicBalance(orderedList: List<Transaction>, userDetails: UserWithExpenses): List<Transaction> {
        orderedList[0].balance = userDetails.userDetails.userCurrentBalance.toDouble()
        orderedList[orderedList.size-1].balance = orderedList[orderedList.size-1].amount
        for (index in 1 until orderedList.size) {
            if (orderedList[index].category == "Income") {
                if(orderedList[index-1].category == "Income"){
                                    orderedList[index].balance =
                    orderedList[index - 1].balance - orderedList[index-1].amount
                }
                else{
                    orderedList[index].balance =
                        orderedList[index - 1].balance + orderedList[index-1].amount
                }
            }
            else{
                orderedList[index].balance = orderedList[index-1].balance + orderedList[index-1].amount
            }
        }
        orderedList[orderedList.size-1].balance = orderedList[orderedList.size-1].amount

        return orderedList
    }
}