package com.example.spendwithbrain.db.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.example.spendwithbrain.db.entities.ExpensesDetails

@Dao
interface ExpenseDAO {
    @Query("UPDATE userDetails SET userCurrentBalance = userCurrentBalance - :new_expense WHERE userId = :id")
    fun updateUserExpense(id: Int, new_expense: Long): Int

    @Query("SELECT SUM(expensesAmount) FROM expensesDetails WHERE expensesDate BETWEEN :startDate AND :endDate GROUP BY :id")
    fun getUserExpenseByDate(startDate: Long, endDate: Long, id: Int): Long

    @Query("SELECT expensesId, userId, expensesDate, SUM(expensesAmount)  AS expensesAmount, expensesCategory, expensesDetails, expensesImage FROM expensesDetails WHERE userId = :id GROUP BY expensesCategory")
    fun getExpenseByCategory(id: Int): List<ExpensesDetails>

//    @Query("")
//    fun getMedianBalance(id: Int): List<ExpensesDetails>
}